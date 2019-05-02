SELECT * FROM user_message WHERE id = 1
UNION
SELECT * FROM user_message WHERE id = 2;

SELECT * INTO new_user_message FROM user_message;

CREATE TABLE new_user_message AS SELECT * FROM user_message;

INSERT INTO new_user_message SELECT * FROM user_message;

SELECT * FROM new_user_message;

DROP TABLE new_user_message;

DELETE FROM new_user_message;

ALTER TABLE user_message ADD COLUMN new_column INT NOT NULL DEFAULT 1;

CREATE VIEW user_message_view AS SELECT app_id, user_id FROM user_message;

SELECT * FROM user_message_view;

SELECT * FROM user_message;

DECLARE 

DELIMITER //
CREATE PROCEDURE user_message_cout_procedure (
	IN userId INT,
	OUT totalCount INT
)
BEGIN
	SELECT COUNT(*) INTO totalCount FROM user_message WHERE user_id = userId;
END//
DELIMITER ;

CALL user_message_cout_procedure(1, @totalCount);
SELECT @totalCount;


DELIMITER //
CREATE PROCEDURE if_procedure (IN i INT, OUT r VARCHAR(255))
BEGIN 
	IF i < 10 THEN
		SET r = '小于10';
	ELSEIF i < 20 THEN
		SET r = '大于10 小于20';
	ELSE 
		SET r = '大于20';
	END IF;
END//
DELIMITER ;

CALL if_procedure(15, @r);

SELECT @r;


DELIMITER //
CREATE PROCEDURE case_procedure(IN i INT, OUT r VARCHAR(255))
BEGIN
	CASE i
		WHEN 1 THEN
			SET r = '等于1';
		WHEN 2 THEN 
			SET r = '等于2';
		WHEN 3 THEN 
			SET r = '等于3';
		ELSE 
			SET r = '不等于1/2/3';
	END CASE;
END//
DELIMITER ;

CALL case_procedure(15, @r);

SELECT @r;


DELIMITER //
CREATE PROCEDURE while_procedure (IN i INT, OUT SUM INT)
BEGIN
	DECLARE s INT;
	DECLARE r INT;
	SET s = 1;
	SET r = 0;
	WHILE s <= i DO
		SET r = r + s;
		SET s = s + 1;
	END WHILE;
	SET SUM = r;
END //
DELIMITER ;


CALL while_procedure(10, @r);

SELECT @r;



DROP PROCEDURE IF EXISTS repeat_procedure;
DELIMITER //
CREATE PROCEDURE repeat_procedure(IN i INT, OUT SUM INT)
BEGIN
	DECLARE s INT;
	DECLARE r INT;
	SET s = 1;
	SET r = 0;
	REPEAT
		SET r = r + s;
		SET s = s + 1;
		UNTIL s > i
	END REPEAT;
	SET SUM = r;
END //
DELIMITER ;

CALL repeat_procedure(10, @r);

SELECT @r;

DROP PROCEDURE IF EXISTS loop_procedure;
DELIMITER //
CREATE PROCEDURE loop_procedure(IN i INT, OUT SUM INT)
BEGIN
	DECLARE s INT;
	DECLARE r INT;
	SET s = 1;
	SET r = 0;
	loop_label:LOOP
		SET r = r + s;
		SET s = s + 1;
		IF s > 10 THEN
			LEAVE loop_label;
		END IF;
	END LOOP;
	SET SUM = r;
END //
DELIMITER ;



CALL loop_procedure(10, @r);

SELECT @r;

DELIMITER //
CREATE PROCEDURE cursor_procedure(OUT app_id VARCHAR(255))
BEGIN
	DECLARE user_message_cursor CURSOR FOR SELECT app_id FROM user_message;
	OPEN user_message_cursor;
	FETCH user_message_cursor INTO app_id;
	CLOSE user_message_cursor;
END //
DELIMITER ;

CALL cursor_procedure(@r);

SELECT @r;

CREATE INDEX user_message_index ON user_message (user_id);

ALTER TABLE user_message DROP INDEX user_message_index;

DELIMITER //
CREATE TRIGGER user_message_trigger BEFORE INSERT ON user_message FOR EACH ROW
BEGIN 
	SET new.app_id = UPPER(new.app_id);
END //
DELIMITER ;


SELECT * FROM user_message;

INSERT INTO user_message VALUES(5, 'a_b_c', 1, 'Something', NOW(), DEFAULT);




