-- Roles
INSERT INTO userapi_role (id, name) VALUES('a060a3bc-5458-4baf-b446-72e2c977f48f', 'ROLE_ADMIN');
INSERT INTO userapi_role (id, name) VALUES('562b4959-2a43-494a-b0e0-2b75810840e7', 'ROLE_EDITOR');

-- Users
INSERT INTO userapi_user (id, enabled, name, password, points)
VALUES('9c21b8c5-0aa2-4839-950f-572a7b727bdc', true, 'admin', '21232f297a57a5a743894a0e4a801fc3', 2000);

INSERT INTO userapi_user (id, enabled, name, password, points)
VALUES('5e9a63db-7658-4d55-b61c-561be0603e87', true, 'editor', '3be8c5739d8f056b124838de345dec56', 1000);

INSERT INTO userapi_user (id, enabled, name, password, points)
VALUES('b9a1295d-042f-43cb-a122-99afe79a20c3', true, 'madara', 'ca0ac6f71f426aa63a4bbfabea06d1a2', 500);

INSERT INTO userapi_user (id, enabled, name, password, points)
VALUES('9c21b8c5-0aa2-4839-950f-572a7b727bdc', true, 'pedrola', 'c25894ebba77ba392a5f9a67354ca257', 0);

-- Roles for Users
INSERT INTO userapi_user_roles (iduser, idrole)
VALUES('9c21b8c5-0aa2-4839-950f-572a7b727bdc', 'a060a3bc-5458-4baf-b446-72e2c977f48f');

INSERT INTO userapi_user_roles (iduser, idrole)
VALUES('5e9a63db-7658-4d55-b61c-561be0603e87', '562b4959-2a43-494a-b0e0-2b75810840e7');

-- Languages
-- INSERT INTO sstarter_language (id, code, flagcode, languagename) VALUES(10000, 'es', 'es', 'Español');
-- INSERT INTO sstarter_language (id, code, flagcode, languagename) VALUES(10001, 'gb', 'gb', 'English');
-- INSERT INTO sstarter_language (id, code, flagcode, languagename) VALUES(10002, 'it', 'it', 'Italiano');
-- INSERT INTO sstarter_language (id, code, flagcode, languagename) VALUES(10003, 'pt', 'pt', 'Português');
-- INSERT INTO sstarter_language (id, code, flagcode, languagename) VALUES(10004, 'de', 'de', 'Deutsch');
-- INSERT INTO sstarter_language (id, code, flagcode, languagename) VALUES(10005, 'ru', 'ru', 'Pусский');
-- INSERT INTO sstarter_language (id, code, flagcode, languagename) VALUES(10006, 'jp', 'jp', '日本の');
-- INSERT INTO sstarter_language (id, code, flagcode, languagename) VALUES(10007, 'cn', 'cn', '中国');
-- INSERT INTO sstarter_language (id, code, flagcode, languagename) VALUES(10008, 'fr', 'fr', 'Français');

