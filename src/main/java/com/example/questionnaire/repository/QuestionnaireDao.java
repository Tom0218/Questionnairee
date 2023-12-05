package com.example.questionnaire.repository;


import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.questionnaire.entity.Questionnaire;

@Repository
public interface QuestionnaireDao extends JpaRepository<Questionnaire,Integer>{

	public List<Questionnaire> findByIdIn(List<Integer> idList);
	
	public List<Questionnaire> findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqual(String title ,LocalDate startDate, LocalDate endDate);
	
	public List<Questionnaire> findByTitleContainingAndStartDateGreaterThanEqualAndEndDateLessThanEqualAndPublishedTrue(String title ,LocalDate startDate, LocalDate endDate);
//因為明確告訴PublishedTru所以不用上參數
	
	@Modifying
	@Transactional
	@Query(value = "insert into questionnaire (title, description, is_published, start_date, end_date)"
			+ "values(:title, :desp, :isPublished, :startDate, :endDate)",nativeQuery = true)
	public int insert(
			@Param("title")String title, //
			@Param("desp")String description,//
			@Param("isPublished")boolean isPublished,//
			@Param("startDate")LocalDate startDate, //
			@Param("endDate")LocalDate endDate );
	
	@Modifying
	@Transactional
	@Query(value = "insert into questionnaire (title, description, is_published, start_date, end_date)"
			+ "values(?1, ?2, ?3, ?4, ?5)",nativeQuery = true)
	public int insertData(
			String title, //
			String description,//
			boolean isPublished,//
			LocalDate startDate, //
			LocalDate endDate );
	
	//=======================================================update
	
	@Modifying(clearAutomatically = true)//clearAutomatically清除持久化上下文，清除暫存資料
	@Transactional
	@Query(value = "update Questionnaire set title = :title, description = :desp"+ "where id = :id",
			nativeQuery = true)
	public int updateData(
			@Param("id")int id,//
			@Param("title")String title,//
			@Param("desp")String description);
	
	@Modifying(clearAutomatically = true)//clearAutomatically清除持久化上下文，清除暫存資料
	@Transactional
	@Query(value = "update Questionnaire set :title = title, description = :desp, startDate = :startDate"
	+"where id = :id", nativeQuery = true)
	public int updateData1(
			@Param("id")int id,//
			@Param("title")String title,//
			@Param("desp")String description);
	
//	@Query(value = "select id, title, is_published from questionnaire"+"where start_date > :startDate",nativeQuery = true);
//	public List<Questionnaire> findByLocalStartDate();
//	
//	
//===============================================select * from
	@Query(value = "select * from questionnaire "
			+ "limit :startIndex, :limitNum", nativeQuery = true)
	public List<Questionnaire> findWithLimitAndStartPosition(
			@Param("startIndex")int startIndex,//
			@Param("limitNum")int limitNum);
	
	@Query(value = "select * from questionnaire " +
			"where title like %:title%", nativeQuery = true)
	public List<Questionnaire> searchTitleLike(@Param("title")String title);
	
	//regexp
	@Query(value = "select * from questionnaire " +
			"where title like regexp:title%", nativeQuery = true)
	public List<Questionnaire> searchTitleLike2(@Param("title")String title);

	//regexp or
	@Query(value = "select * from questionnaire "+
			" where  description regexp:keyword1|:keyword2 ", nativeQuery = true)
	public List<Questionnaire> searchDescriptionContaining (
			@Param("keyword1")String keyword1,//
			@Param("keyword2")String keyword2);
	
//	@Query(value="update Questionnaire set published = true where*selcet")
//	public int updateQnStatus(@Param("today")LocalDate today);
}

