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
import com.example.questionnaire.service.ifs.QuizService;
import com.example.questionnaire.vo.QuestionRes;
import com.example.questionnaire.vo.QuestionnaireRes;
import com.example.questionnaire.vo.QuizReq;
import com.example.questionnaire.vo.QuizRes;
import com.example.questionnaire.vo.QuizVo;

@EnableScheduling
@Service
public class QuizServiceImpl implements QuizService {

	@Autowired
	private QuestionnaireDao qnDao;

	@Autowired
	private QuestionDao quDao;

	@Transactional // Transactional����x�s�ɥ������ѩΥ������\�A����[�bpriavte�v��
	@Override
	public QuizRes create(QuizReq req) {
		QuizRes checkResult = checkParam(req);
		List<Question> quList = req.getQuestionList();
		if (checkResult != null) {
			return checkResult;
		}
		int quId = qnDao.save(req.getQuestionnaire()).getId();
		// �i�H�إߨS���D�ت��ݨ�
		if (quList.isEmpty()) {
			return new QuizRes(RtnCode.SUCCESSFUL);
		}
		for (Question qu : quList) {
			qu.setQnId(quId);
		}
		quDao.saveAll(quList);
		return new QuizRes(RtnCode.SUCCESSFUL);
	}

	// �q�w�ˬd�ѼƤ�k�H��֥D�{��create�{���X��ơC
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
			// �P�_Question �̪�qnid ���S������Questionniare��id�H����e����s
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

		// �`���N�Q�R����quId
		List<Integer> deleteQuIdList = new ArrayList<>();
		for (Question qu : req.getDeleteQuestionList()) {
			deleteQuIdList.add(qu.getQuId());
		}
		// �i�ק����!!
		// 1.�|���o��:is_published = false�A�i�H�ק�
		// 2.�w�o�����|���}�l�i��is_published = true + ��e�ɶ������p��start_date
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
		// �P�_Id�O�_�s�b�_�h�Yid���s�b�h�|�s�W�Ӥ��O�ק�
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
			quDao.deleteAllByQnIdIn(idList);// �R�ݨ��̪��D��
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
//			System.out.print(qu.getId());// ���X�ŦX����qnid
			qnIds.add(qu.getId());
		}
		List<Question> quList = quDao.findAllByQnIdIn(qnIds); // ��X�Ҧ��ŦX����qnid�����D
		List<QuizVo> quizVoList = new ArrayList<>();
		for (Questionnaire qn : qnList) {
			QuizVo vo = new QuizVo();// �إߤ@�Ӱݨ�
			vo.setQuestionnaire(qn);// ��ŦX���󪺤@�Ӱݨ�set�iQuestionnaire

			List<Question> questionList = new ArrayList<>();// �إߩ���D��list�C
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
				user.getQnId() > 0 || user.getqId() > 0 || StringUtils.hasText(user.getAnswer())
				|| user.getDateTime() != null) {
			System.out.println("name" + user.getName());
			System.out.println(user.getPhoneNumber());
			System.out.println(user.getAge());
			System.out.println(user.getEmail());

			return new QuizRes(RtnCode.SUCCESSFUL);
		}
		System.out.println("name" + user.getName());
		System.out.println("phone" + user.getPhoneNumber());
		System.out.println("age" + user.getAge());
		System.out.println("email" + user.getEmail());
		return new QuizRes(RtnCode.UPDATE_ERROR);
	}

//	( �� �� �� �� �� �P )
	@Scheduled(cron = "0 * 14 * * *")
	public void schedule() {
		System.out.println(LocalDateTime.now());
	}
	

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
