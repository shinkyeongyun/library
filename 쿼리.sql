CREATE DATABASE db_library;
USE db_library;

CREATE TABLE tbl_admin_member(
	a_m_no		INT 	AUTO_INCREMENT, 
	a_m_approval	INT	NOT NULL DEFAULT 0, 
	a_m_id		VARCHAR(20) 	NOT NULL, 
	a_m_pw		VARCHAR(100) 	NOT NULL, 
	a_m_name	VARCHAR(20) 	NOT NULL, 
	a_m_gender	CHAR(1) 	NOT NULL, 
	a_m_part	VARCHAR(20) 	NOT NULL,
	a_m_position	VARCHAR(20) 	NOT NULL,
	a_m_mail	VARCHAR(50) 	NOT NULL, 
	a_m_phone	VARCHAR(20) 	NOT NULL, 
	a_m_reg_date 	DATETIME, 
	a_m_mod_date	DATETIME, 
	PRIMARY KEY(a_m_no)
	);
	


CREATE TABLE tbl_book(
	b_no			INT	AUTO_INCREMENT,
	b_thumbnail		VARCHAR(100),
	b_name			VARCHAR(30)	NOT NULL, 	
	b_author		VARCHAR(20)	NOT NULL, 	
	b_publisher		VARCHAR(20)	NOT NULL,
	b_publish_year		CHAR(4)		NOT NULL, 	
	b_isbn			VARCHAR(30)	NOT NULL, 
	b_call_number		VARCHAR(30)	NOT NULL, 
	b_rantal_able		TINYINT	NOT NULL DEFAULT 1, 
	b_reg_date		DATETIME,
	b_mod_date		DATETIME,
	PRIMARY KEY(b_no)
	)DEFAULT CHARSET=utf8;



CREATE TABLE tbl_user_member(
	u_m_no		INT 	AUTO_INCREMENT, 
	u_m_id		VARCHAR(20) 	NOT NULL, 
	u_m_pw		VARCHAR(100) 	NOT NULL, 
	u_m_name	VARCHAR(20) 	NOT NULL, 
	u_m_gender	CHAR(1) NOT NULL, 
	u_m_mail	VARCHAR(50) 	NOT NULL, 
	u_m_phone	VARCHAR(20) 	NOT NULL, 
	u_m_reg_date 	DATETIME, 
	u_m_mod_date	DATETIME, 
	PRIMARY KEY(u_m_no)
	);
	
	
	
	CREATE TABLE tbl_rental_book(
    rb_no            INT AUTO_INCREMENT,
    b_no             INT,
    u_m_no           INT,
    rb_start_date    DATETIME,
    rb_end_date      DATETIME DEFAULT '1000-01-01',
    rb_reg_date      DATETIME,
    rb_mod_date      DATETIME,
    PRIMARY KEY(rb_no)
);

CREATE TABLE tbl_hope_book(
	hb_no 		     INT		AUTO_INCREMENT, 
	u_m_no		     INT, 
	hb_name		     VARCHAR(30)	NOT NULL, 	
	hb_author	     VARCHAR(20)	NOT NULL, 	
	hb_publisher	     VARCHAR(20),
	hb_publish_year	     CHAR(4), 
	hb_reg_date	     DATETIME, 
	hb_mod_date	     DATETIME, 
	hb_result	     TINYINT	        NOT NULL DEFAULT 0, 
	hb_result_last_date  DATETIME,
	PRIMARY KEY(hb_no)
)DEFAULT CHARSET=utf8;

	SELECT * FROM tbl_admin_member;
	SELECT * FROM tbl_book;
	SELECT * FROM tbl_user_member;
	SELECT * FROM tbl_rental_book;
	SELECT * FROM tbl_hope_book;





	