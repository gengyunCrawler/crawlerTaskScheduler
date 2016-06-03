CREATE TABLE crawlerTaskUserTable
(
  userId VARCHAR(64) NOT NULL,
  userAppkey TEXT,
  userDescription TEXT,
  PRIMARY KEY (userId)

);


CREATE TABLE crawlerTaskTable
(
  userId VARCHAR(64) NOT NULL,
  taskId VARCHAR(64) NOT NULL,
  taskType ENUM('UNSET', 'TOPIC', 'WHOLESITE') DEFAULT 'UNSET' NOT NULL,
  taskRemark TEXT,
  taskSeedUrl TEXT,
  taskCrawlerDepth INT,
  taskDynamicDepth INT,
  taskWeight INT,
  taskStartTime TIMESTAMP,
  taskRecrawlerDays INT,
  taskTemplatePath TEXT,
  taskTagPath TEXT,
  taskProtocolFilter TEXT,
  taskSuffixFilter TEXT,
  taskRegexFilter TEXT,
  taskStatus ENUM('UNCRAWL', 'CRAWLING', 'CRAWLED', 'EXCEPTIOSTOP') DEFAULT 'UNCRAWL' NOT NULL,
  taskDeleteFlag BOOL DEFAULT FALSE,
  taskSeedAmount INT,
  taskSeedImportAmount INT,
  taskCompleteTimes INT,
  taskInstanceThreads  INT DEFAULT 1 NOT NULL,
  taskNodeInstances  INT DEFAULT 1 NOT NULL,
  taskStopTime TIMESTAMP,
  taskCrawlerAmountInfo TEXT,
  taskCrawlerInstanceInfo TEXT,
  PRIMARY KEY (taskId)
);