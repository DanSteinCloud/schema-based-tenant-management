

CREATE TABLE assiganto.BATCH_JOB_INSTANCE  (
                                     JOB_INSTANCE_ID BIGINT  NOT NULL PRIMARY KEY ,
                                     VERSION BIGINT ,
                                     JOB_NAME VARCHAR(100) NOT NULL,
                                     JOB_KEY VARCHAR(32) NOT NULL,
                                     constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
) ;

CREATE TABLE assiganto.BATCH_JOB_EXECUTION  (
                                      JOB_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
                                      VERSION BIGINT  ,
                                      JOB_INSTANCE_ID BIGINT NOT NULL,
                                      CREATE_TIME TIMESTAMP NOT NULL,
                                      START_TIME TIMESTAMP DEFAULT NULL ,
                                      END_TIME TIMESTAMP DEFAULT NULL ,
                                      STATUS VARCHAR(10) ,
                                      EXIT_CODE VARCHAR(2500) ,
                                      EXIT_MESSAGE VARCHAR(2500) ,
                                      LAST_UPDATED TIMESTAMP,
                                      constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
                                          references assiganto.BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
) ;

CREATE TABLE assiganto.BATCH_JOB_EXECUTION_PARAMS  (
                                             JOB_EXECUTION_ID BIGINT NOT NULL ,
                                             PARAMETER_NAME VARCHAR(100) NOT NULL ,
                                             PARAMETER_TYPE VARCHAR(100) NOT NULL ,
                                             PARAMETER_VALUE VARCHAR(2500) ,
                                             IDENTIFYING CHAR(1) NOT NULL ,
                                             constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
                                                 references assiganto.BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;

CREATE TABLE assiganto.BATCH_STEP_EXECUTION  (
                                       STEP_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
                                       VERSION BIGINT NOT NULL,
                                       STEP_NAME VARCHAR(100) NOT NULL,
                                       JOB_EXECUTION_ID BIGINT NOT NULL,
                                       CREATE_TIME TIMESTAMP NOT NULL,
                                       START_TIME TIMESTAMP DEFAULT NULL ,
                                       END_TIME TIMESTAMP DEFAULT NULL ,
                                       STATUS VARCHAR(10) ,
                                       COMMIT_COUNT BIGINT ,
                                       READ_COUNT BIGINT ,
                                       FILTER_COUNT BIGINT ,
                                       WRITE_COUNT BIGINT ,
                                       READ_SKIP_COUNT BIGINT ,
                                       WRITE_SKIP_COUNT BIGINT ,
                                       PROCESS_SKIP_COUNT BIGINT ,
                                       ROLLBACK_COUNT BIGINT ,
                                       EXIT_CODE VARCHAR(2500) ,
                                       EXIT_MESSAGE VARCHAR(2500) ,
                                       LAST_UPDATED TIMESTAMP,
                                       constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
                                           references assiganto.BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;

CREATE TABLE assiganto.BATCH_STEP_EXECUTION_CONTEXT  (
                                               STEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
                                               SHORT_CONTEXT VARCHAR(2500) NOT NULL,
                                               SERIALIZED_CONTEXT TEXT ,
                                               constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
                                                   references assiganto.BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
) ;

CREATE TABLE assiganto.BATCH_JOB_EXECUTION_CONTEXT  (
                                              JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
                                              SHORT_CONTEXT VARCHAR(2500) NOT NULL,
                                              SERIALIZED_CONTEXT TEXT ,
                                              constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
                                                  references assiganto.BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;

CREATE SEQUENCE assiganto.BATCH_STEP_EXECUTION_SEQ MAXVALUE 9223372036854775807 NO CYCLE;
CREATE SEQUENCE assiganto.BATCH_JOB_EXECUTION_SEQ MAXVALUE 9223372036854775807 NO CYCLE;
CREATE SEQUENCE assiganto.BATCH_JOB_SEQ MAXVALUE 9223372036854775807 NO CYCLE;

CREATE TABLE assiganto.product_review (
                                review_id SERIAL PRIMARY KEY,                 -- Unique identifier for each review
                                product_id INT NOT NULL,                     -- Reference to the product (foreign key to product table)
                                user_id INT NOT NULL,                        -- Identifier for the user who made the review
                                rating SMALLINT CHECK (rating BETWEEN 1 AND 5), -- Rating between 1 and 5
                                review_text TEXT,                            -- Text of the review
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Timestamp for when the review was created
                                is_processed BOOLEAN DEFAULT FALSE           -- Flag to indicate if the review has been processed
);

CREATE INDEX idx_created_at ON assiganto.product_review (created_at);

INSERT INTO assiganto.product_review (product_id,user_id,rating,review_text,created_at,is_processed) VALUES
                                                                                                      (95,652,1,'Templum ante aurum.','2024-12-12 09:00:19.386118',false),
                                                                                                      (74,822,2,'Quae adeptio corpus.','2024-12-12 09:00:19.460451',false),
                                                                                                      (85,341,4,'Non ascisco advoco comedo peccatus.','2024-12-12 09:00:19.508281',false),
                                                                                                      (34,636,5,'Celebrer ambitus urbanus titulus catena comparo adduco supra somniculosus crapula.','2024-12-12 09:00:19.55874',false),
                                                                                                      (90,412,3,'Vomer suus placeat.','2024-12-12 09:00:19.610803',false),
                                                                                                      (92,51,1,'Tero illo pauci tego pax commodo casus articulus surgo.','2024-12-12 09:00:19.663565',false),
                                                                                                      (56,663,2,'Minus aveho alienus benigne.','2024-12-12 09:00:19.72278',false),
                                                                                                      (3,278,3,'Testimonium caelum trans texo.','2024-12-12 09:00:19.780229',false),
                                                                                                      (32,694,3,'Solutio abutor cena abundans tumultus tendo.','2024-12-12 09:00:19.828737',false),
                                                                                                      (77,180,5,'Tolero sponte ambitus adicio possimus crur stipes quae suadeo tremo.','2024-12-12 09:00:19.87992',false);