package com.example.questionnaire.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.questionnaire.constants.RtnCode;
import com.example.questionnaire.entity.Question;
import com.example.questionnaire.entity.Questionnaire;
import com.example.questionnaire.repository.QuestionDao;
import com.example.questionnaire.repository.QuestionnaireDao;
import com.example.questionnaire.service.ifs.QuizService;
import com.example.questionnaire.vo.QuestionRes;
import com.example.questionnaire.vo.QuestionnaireRes;
import com.example.questionnaire.vo.QuizReq;
import com.example.questionnaire.vo.QuizRes;
import com.example.questionnaire.vo.QuizVo;

@Service
public class QuizServiceImpl implements QuizService {

	@Autowired
	private QuestionnaireDao qnDao;

	@Autowired
	private QuestionDao quDao;
	// Transactional����x�s�ɥ������ѩΥ������\�A����[�bpriavte�v��
	@Transactional
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
				|| qn.getStartDate() == null || qn.getEndDate() == null 
				|| qn.getStartDate().isAfter(qn.getEndDate())) {
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
		return null;
	}

	@Transactional
	@Override
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
		
		// �i�ק����!!
		// 1.�|���o��:is_published = false�A�i�H�ק�
		// 2.�w�o�����|���}�l�i��is_published = true + ��e�ɶ������p��start_date
		Questionnaire qn = qnOp.get();
		if (!qn.isPublished() || (qn.isPublished() && LocalDate.now().isBefore(qn.getStartDate()))) {
			qnDao.save(req.getQuestionnaire());
			quDao.saveAll(req.getQuestionList());
			return new QuizRes(RtnCode.SUCCESSFUL);
		}
		return new QuizRes(RtnCode.UPDATE_ERROR);
		// �P�_Id�O�_�s�b�_�h�Yid���s�b�h�|�s�W�Ӥ��O�ק�
	}

	

	@Override
	public QuizRes deleteQuestionnaire(List<Integer> qnIdList) { 
		List<Questionnaire> qnList = qnDao.findByIdIn(qnIdList);
		List<Integer> idList = new ArrayList<>();
		for (Questionnaire qn : qnList) {
			if (!qn.isPublished() || qn.isPublished() && LocalDate.now().isBefore(qn.getStartDate())) {
				idList.add(qn.getId());
				System.out.println("1");
			}
		}
		if (!idList.isEmpty()) {
			qnDao.deleteAllById(idList);
//			quDao.deleteAllByQnId(idList);// �R�ݨ��̪��D��
		}
		return new QuizRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public QuizRes deleteQuestion(int qnId, List<Integer> quIdList) {
		Optional<Questionnaire> qnOp = qnDao.findById(qnId);
		if (qnOp.isEmpty()) {
			return new QuizRes(RtnCode.SUCCESSFUL);
		}
		Questionnaire qn = qnOp.get();
		if (!qn.isPublished() || qn.isPublished() && LocalDate.now().isBefore(qn.getStartDate())) {

			quDao.deleteAllByQnIdAndQuIdIn(quIdList); // �R���ݨ���Qnid�MQuid
		}
		return new QuizRes(RtnCode.SUCCESSFUL);
	}

	@Override
	public QuizRes search(String title, LocalDate startDate, LocalDate endDate) {
		title = StringUtils.hasText(title) ? title : "";
		startDate = startDate == null ? LocalDate.of(1971, 1, 1) : startDate;
		endDate = endDate == null ? LocalDate.of(2099, 12, 31) : endDate;
		List<Questionnaire> qnList = qnDao
				.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(title, startDate, endDate);
		List<Integer> qnIds = new ArrayList<>();
		for (Questionnaire qu : qnList) { // ���X�ŦX����qnid
			qnIds.add(qu.getId());
		}
		List<Question> quList = quDao.findAllByQnIdIn(qnIds); // ��X�Ҧ��ŦX����qnid�����D
		List<QuizVo> quizVoList = new ArrayList<>();
		for (Questionnaire qn : qnList) {
			QuizVo vo = new QuizVo();// �ݥߤ@�Ӱݨ�
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
		return new QuizRes(RtnCode.SUCCESSFUL);

	}

	@Override
	public QuestionnaireRes searchQuestionnaireList(String title, LocalDate startDate, LocalDate endDate, boolean isAll) {
		title = StringUtils.hasText(title) ? title : "";
		startDate = startDate == null ? LocalDate.of(1971, 1, 1) : startDate;
		endDate = endDate == null ? LocalDate.of(2099, 12, 31) : endDate;
		List<Questionnaire> qnList = new ArrayList<>();
			if(!isAll) {
				qnList= qnDao.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqualAndPublishedTrue(title, startDate, endDate); 
			} else {
				qnList = qnDao.findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(title, startDate, endDate);
			}
		return new QuestionnaireRes(qnList, RtnCode.SUCCESSFUL);
	}

	@Override
	public QuestionRes searchQuestionList(int qnId) {
		if(qnId <= 0) {
			return new QuestionRes(null, RtnCode.QUESTIONNAIRE_ID_PARAM_ERROR);
		}
		List<Question> quList = quDao.findAllByQnIdIn(Arrays.asList(qnId));
		return new QuestionRes(quList,RtnCode.SUCCESSFUL);
	}

}
