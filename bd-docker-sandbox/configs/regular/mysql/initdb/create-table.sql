-- Licensed to the Apache Software Foundation (ASF) under one
-- or more contributor license agreements.  See the NOTICE file
-- distributed with this work for additional information
-- regarding copyright ownership.  The ASF licenses this file
-- to you under the Apache License, Version 2.0 (the
-- "License"); you may not use this file except in compliance
-- with the License.  You may obtain a copy of the License at
-- 
--     http://www.apache.org/licenses/LICENSE-2.0
-- 
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.

CREATE DATABASE SANDBOXDB;

CREATE USER 'flink'@'%' IDENTIFIED BY 'p@SSW0rd';
REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'flink'@'%';
GRANT ALL PRIVILEGES ON SANDBOXDB.* TO 'flink'@'%';

FLUSH PRIVILEGES;

USE SANDBOXDB;

CREATE TABLE spend_report (
	account_id BIGINT NOT NULL,
	log_ts     TIMESTAMP(3) NOT NULL,
	amount	   BIGINT NOT NULL,
	PRIMARY KEY (account_id, log_ts)
);
