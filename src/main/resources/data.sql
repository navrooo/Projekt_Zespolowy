INSERT INTO dbo.Turysta (imie, nazwisko, telefon, id_uzytkownik, opis, punkty)
VALUES ('Jan', 'Kowalski', '695162612', 1, '', 0);

INSERT INTO dbo.Uzytkownik (login, haslo, email, id_rola)
VALUES ('test','$2a$10$gkGHoPBqY7sGdHdVBmveuucoNlAyHYVZkLFui.1tRJWfAxV40jIPW','test@o2.pl', 1)

INSERT INTO dbo.Rola (nazwa)
VALUES('ROLE_administrator');