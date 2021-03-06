DROP TABLE IF EXISTS PRICES;

CREATE TABLE PRICES (
  ID BIGINT AUTO_INCREMENT PRIMARY KEY,
  BRAND_ID BIGINT NOT NULL,
  START_DATE TIMESTAMP NOT NULL,
  END_DATE TIMESTAMP DEFAULT NULL,
  PRICE_LIST BIGINT NOT NULL,
  PRODUCT_ID BIGINT NOT NULL,
  PRIORITY SMALLINT NOT NULL,
  PRICE DECIMAL(20, 2) NOT NULL,
  CURR VARCHAR(3) NOT NULL
);