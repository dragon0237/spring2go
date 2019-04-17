-- create the database structure for OAuth2
--客户端的注册信息表
create table oauth_client_details (
  client_id VARCHAR(256) PRIMARY KEY,
  resource_ids VARCHAR(256),
  client_secret VARCHAR(256),
  scope VARCHAR(256),
  authorized_grant_types VARCHAR(256),
  web_server_redirect_uri VARCHAR(256),
  authorities VARCHAR(256),
  access_token_validity INTEGER,
  refresh_token_validity INTEGER,
  additional_information VARCHAR(4096),
  autoapprove VARCHAR(256)
);
--令牌存储表
create table oauth_access_token (
  token_id VARCHAR(256),
  token BINARY,
  authentication_id VARCHAR(256) PRIMARY KEY,
  user_name VARCHAR(256),
  client_id VARCHAR(256),
  authentication BINARY,
  refresh_token VARCHAR(256)
);
--记录oauth批准授权信息
create table oauth_approvals (
    userId VARCHAR(256),
    clientId VARCHAR(256),
    scope VARCHAR(256),
    status VARCHAR(10),
    expiresAt TIMESTAMP,
    lastModifiedAt TIMESTAMP
);

-- insert a default client credentials
--预注册一个客户端的相关信息
insert into oauth_client_details
(client_id, client_secret, scope,
 authorized_grant_types, web_server_redirect_uri)
values
('mobileclient', '112233', 'read_userinfo,read_contacts',
'authorization_code,implicit,password', 'oauth2://userinfo/callback,http://localhost:9000/callback'
);