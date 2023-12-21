package com.example.questionnaire.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.Questionnaire;
import com.example.questionnaire.entity.User;
import com.example.questionnaire.repository.QuestionDao;
import com.example.questionnaire.repository.QuestionnaireDao;
import com.example.questionnaire.repository.UserDao;
import com.example.questionnaire.service.ifs.QuizService;
import com.example.questionnaire.vo.QuestionRes;
import com.example.questionnaire.vo.QuestionnaireRes;
import com.example.questionnaire.vo.QuizReq;
import com.example.questionnaire.vo.QuizRes;
import com.example.questionnaire.vo.QuizVo;
import com.example.questionnaire.vo.UserReq;
import com.example.questionnaire.vo.UserRes;

@EnableScheduling
@Service
public class QuizServiceImpl implements QuizService {

	@Autowired
	private QuestionnaireDao qnDao;

	@Autowired
	private QuestionDao quDao;

	@Autowired
	private UserDao userDao;

	@Transactional // Transactional跨表儲存時全部失敗或全部成功，不能加在priavte權限
	@Override
	public QuizRes create(QuizReq req) {
		QuizRes checkResult = checkParam(req);
		List<Question> quList = req.getQuestionList();
		if (checkResult != null) {
			return checkResult;
		}
		int quId = qnDao.save(req.getQuestionnaire()).getId();
		// 可以建立沒有題目的問卷
		if (quList.isEmpty()) {
			return new QuizRes(RtnCode.SUCCESSFUL);
		}
		for (Question qu : quList) {
			qu.setQnId(quId);
		}
		quDao.saveAll(quList);
		return new QuizRes(RtnCode.SUCCESSFUL);
	}

	// 訂定檢查參數方法以減少主程式create程式碼行數。
	private QuizRes checkParam(QuizReq req) {
		Questionnaire qn = req.getQuestionnaire();
		if (!StringUtils.hasText(qn.getTitle()) || !StringUtils.hasText(qn.getDescription())
				|| qn.getStartDate() == null || qn.getEndDate() == null || qn.getStartDate().isAfter(qn.getEndDate())) {
			return new QuizRes(RtnCode.QUESTIONNAIRE_PARAM_ERROR);
		}
		List<Question> quList = req.getQuestionList();
		for (Question qu : quList) {
			if (qu.getQuId() <= 0 || !StringUtils.hasText(qu.getqTitle())) {
				return new QuizRes(RtnCode.QUESTION_PARAM_ERROR);
			}
		}
		return null;
	}

	private QuizRes checkQuestionniareId(QuizReq req) {
		if (req.getQuestionnaire().getId() <= 0) {
			return new QuizRes(RtnCode.QUESTIONNAIRE_ID_PARAM_ERROR);
		}
		List<Question> quList = req.getQuestionList();
		for (Question qu : quList) {
			// 判斷Question 裡的qnid 有沒有等於Questionniare的id以防止送錯更新
			if (qu.getQnId() != req.getQuestionnaire().getId()) {
				return new QuizRes(RtnCode.QUESTIONNAIRE_ID_PARAM_ERROR);
			}
		}
		List<Question> quDelList = req.getDeleteQuestionList();
		for (Question qu : quDelList) {
			if (qu.getQnId() != req.getQuestionnaire().getId()) {
				return new QuizRes(RtnCode.QUESTIONNAIRE_ID_PARAM_ERROR);
			}
		}
		return null;
	}

	@Transactional
	@Override
	@CacheEvict(cacheNames = "update", allEntries = true)
	public QuizRes update(QuizReq req) {

		QuizRes checkResult = checkParam(req);
		if (checkResult != null) {
			return checkResult;
		}

		checkResult = checkQuestionniareId(req);
		if (checkResult != null) {
			return checkResult;
		}

		Optional<Questionnaire> qnOp = qnDao.findById(req.getQuestionnaire().getId());
		if (qnOp.isEmpty()) {
			return new QuizRes(RtnCode.QUESTIONNAIRE_ID_NOT_FOUND);
		}

		// 蒐集將被刪除的quId
		List<Integer> deleteQuIdList = new ArrayList<>();
		for (Question qu : req.getDeleteQuestionList()) {
			deleteQuIdList.add(qu.getQuId());
		}
		// 可修改條件!!
		// 1.尚未發布:is_published = false，可以修改
		// 2.已發布但尚未開始進行is_published = true + 當前時間必須小於start_date
		Questionnaire qn = qnOp.get();
		if (!qn.isPublished() || (qn.isPublished() && LocalDate.now().isBefore(qn.getStartDate()))) {
			qnDao.save(req.getQuestionnaire());
			quDao.saveAll(req.getQuestionList());
			if (!deleteQuIdList.isEmpty()) {
				quDao.deleteAllByQnIdAndQuIdIn(qn.getId(), deleteQuIdList);
			}
			return new QuizRes(RtnCode.SUCCESSFUL);
		}
		return new QuizRes(RtnCode.UPDATE_ERROR);
		// 判斷Id是否存在否則若id不存在則會新增而不是修改
	}

	@Transactional
	@Override
	@CacheEvict(cacheNames = "deleteQuestionnaire", allEntries = true)
	public QuizRes deleteQuestionnaire(List<Integer> qnIdList) {
		List<Questionnaire> qnList = qnDao.findByIdIn(qnIdList);
		List<Integer> idList = new ArrayList<>();
		for (Questionnaire qn : qnList) {
			if (!qn.isPublished() || qn.isPublished() && LocalDate.now().isBefore(qn.getStartDate())) {
				idList.add(qn.getId());
			}
		}
		if (!idList.isEmpty()) {
			qnDao.deleteAllById(idList);
			quDao.deleteAllByQnIdIn(idList);// 刪問卷裡的題目
		}
		return new QuizRes(RtnCode.SUCCESSFUL);
	}

	@Transactional
	@Override
	@CacheEvict(cacheNames = "deleteQuestion", allEntries = true)
	public QuizRes deleteQuestion(int qnId, List<Integer> quIdList) {
		Optional<Questionnaire> qnOp = qnDao.findById(qnId);
		if (qnOp.isEmpty()) {
			return new QuizRes(RtnCode.SUCCESSFUL);
		}
		Questionnaire qn = qnOp.get();
		if (!qn.isPublished() || qn.isPublished() && LocalDate.now().isBefore(qn.getStartDate())) {
			quDao.deleteAllByQnIdAndQuIdIn(qn.getId(), quIdList);
		}
		return new QuizRes(RtnCode.SUCCESSFUL);
	}

	@Transactional
	@Cacheable(cacheNames = "search",
			// key = "#title"_#startDate_#endDate
			key = "#title.concat('_').concat(#startDate.toString()).concat('_').concat(#endDate.toString())", unless = "#result.rtncode.code !=200")
	@CacheEvict(cacheNames = "search", allEntries = true)
	@Override
	public QuizRes search(String title, LocalDate startDate, LocalDate endDate) {
		List<Questionnaire> qnList = qnDao
				.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(title, startDate, endDate);
		List<Integer> qnIds = new ArrayList<>();
		for (Questionnaire qu : qnList) {
//			System.out.print(qu.getId());// 取出符合條件的qnid
			qnIds.add(qu.getId());
		}
		List<Question> quList = quDao.findAllByQnIdIn(qnIds); // 找出所有符合條件的qnid的問題
		List<QuizVo> quizVoList = new ArrayList<>();
		for (Questionnaire qn : qnList) {
			QuizVo vo = new QuizVo();// 建立一個問卷
			vo.setQuestionnaire(qn);// 把符合條件的一個問卷set進Questionnaire

			List<Question> questionList = new ArrayList<>();// 建立放問題的list。
			for (Question qu : quList) {
				if (qu.getQnId() == qn.getId()) {
					questionList.add(qu);
				}
			}
			vo.setQuestionList(questionList);
			quizVoList.add(vo);
		}

		return new QuizRes(quizVoList, RtnCode.SUCCESSFUL);
	}

	@Override
	public QuestionnaireRes searchQuestionnaire(int qnId) {
		return null;

	}

	@Cacheable(cacheNames = "searchQuestionnaireList", key = "#title.concat('_').concat(#startDate.toString()).concat('_').concat(#endDate.toString())", unless = "#result.rtncode.code !=200")
	@CacheEvict(cacheNames = "searchQuestionnaireList", allEntries = true)
	@Override
	public QuestionnaireRes searchQuestionnaireList(String title, LocalDate startDate, LocalDate endDate,
			boolean isAll) {
		title = StringUtils.hasText(title) ? title : "";
		startDate = startDate == null ? LocalDate.of(1971, 1, 1) : startDate;
		endDate = endDate == null ? LocalDate.of(2099, 12, 31) : endDate;
		List<Questionnaire> qnList = new ArrayList<>();
		if (!isAll) {
			qnList = qnDao.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqualAndPublishedTrue(
					title, startDate, endDate);
		} else {
			qnList = qnDao.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(title, startDate,
					endDate);
		}
		return new QuestionnaireRes(qnList, RtnCode.SUCCESSFUL);
	}

	@Cacheable(cacheNames = "searchQuestionList")
	@CacheEvict(cacheNames = "searchQuestionList", allEntries = true)
	@Override
	public QuestionRes searchQuestionList(int qnId) {
//		System.out.println("in");
		if (qnId <= 0) {
			return new QuestionRes(null, RtnCode.QUESTIONNAIRE_ID_PARAM_ERROR);
		}
		List<Question> quList = quDao.findAllByQnIdIn(Arrays.asList(qnId));
		return new QuestionRes(quList, RtnCode.SUCCESSFUL);
	}

	@Override
	public QuizRes setInfoAndAnswer(User user) {
		if (StringUtils.hasText(user.getName()) || StringUtils.hasText(user.getPhoneNumber())
				|| StringUtils.hasText(user.getEmail()) || user.getAge() < 0 || // Additional check for age
				user.getQnId() > 0 || user.getQuId() > 0 || StringUtils.hasText(user.getAnswer())
				|| user.getDateTime() != null) {

			return new QuizRes(RtnCode.SUCCESSFUL);
		}
		System.out.println("name" + user.getName());
		System.out.println("phone" + user.getPhoneNumber());
		System.out.println("age" + user.getAge());
		System.out.println("email" + user.getEmail());
		return new QuizRes(RtnCode.UPDATE_ERROR);
	}

//	( 秒 分 時 日 月 周 )
//	@Scheduled(cron = "0 * 14 * * *")
//	public void schedule() {
//		System.out.println(LocalDateTime.now());
//	}

	@Transactional
	@Override
	public UserRes QuestionnaireSubmission(UserReq req) {
		List<User> userSubmissionList = req.getUserSubmissionList();
		System.out.println(userSubmissionList);
		if (userSubmissionList.isEmpty()) {
			return new UserRes(RtnCode.CAN_NOT_BE_EMPTY);
		}
		userDao.saveAll(userSubmissionList);
		return new UserRes(RtnCode.SUCCESSFUL);
	}

	@Transactional
	@Override
	public UserRes Submission(UserReq req) {
		List<User> SubminList = req.getUserSubmissionList();
		userDao.saveAll(SubminList);
		return new UserRes(RtnCode.SUCCESSFUL);

	}

	@Transactional
	@CacheEvict(cacheNames = "getSubmission", allEntries = true)
	@Override
	public UserRes getSubmission(int qnId) {
		if (qnId < 0) {
			return new UserRes(RtnCode.QUESTION_PARAM_ERROR);
		}

		List<User> List = userDao.findAllByQnId(qnId);
		if (List.isEmpty()) {
			return new UserRes(RtnCode.QUESTIONNAIRE_ID_NOT_FOUND);
		}
		return new UserRes(RtnCode.SUCCESSFUL, List);
	}

//	@Cacheable(cacheNames = "searchQuestionList")
//	@CacheEvict(cacheNames = "searchQuestionList", allEntries = true)
//	@Override
//	public QuestionRes searchQuestionList(int qnId) {
////		System.out.println("in");
//		if (qnId <= 0) {
//			return new QuestionRes(null, RtnCode.QUESTIONNAIRE_ID_PARAM_ERROR);
//		}
//		List<Question> quList = quDao.findAllByQnIdIn(Arrays.asList(qnId));
//		return new QuestionRes(quList, RtnCode.SUCCESSFUL);
//	}
//	

//	public void upadateQnStatue() {
//		LocalDate today = LocalDate.now();
//		int res = qnDao.updateQnStatus(today);
//		System.out.print(today);
//		System.out.print(res);
//	}

//	@Override 
//	QuizRes searchFuzzy(String title, LocalDate startDate, LocalDate endDate) {
//		List<QnQuVo> res = qnDao.
//				return new QuizRes(null, res, RtnCode.SUCCESSFUL);
//	}

}
