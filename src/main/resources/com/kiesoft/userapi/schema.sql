CREATE TABLE public.userapi_role (
  id uuid NOT NULL,
  name varchar(255) NULL,
  CONSTRAINT uk1jsewi2hygj7w9k0fyc2sh68p UNIQUE (name),
  CONSTRAINT userapi_role_pkey PRIMARY KEY (id)
);

CREATE TABLE public.userapi_user (
  id uuid NOT NULL,
  enabled bool NULL,
  name varchar(255) NULL,
  password varchar(255) NULL,
  points int4 NULL,
  CONSTRAINT uk165bo9gneneiv5lmjd7o538g3 UNIQUE (name),
  CONSTRAINT userapi_user_pkey PRIMARY KEY (id)
);

CREATE TABLE public.userapi_user_roles (
  iduser uuid NOT NULL,
  idrole uuid NOT NULL,
  CONSTRAINT fkpwirj1cjfwbibb4e5v9gnb1ru FOREIGN KEY (idrole) REFERENCES userapi_role(id),
  CONSTRAINT fkscx4lyepneepsl9w35ni402w9 FOREIGN KEY (iduser) REFERENCES userapi_user(id)
);


