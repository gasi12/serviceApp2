INSERT INTO APP_USER (ID,PHONE_NUMBER,NAME) VALUES
                                                      ('1', '123456789', 'Ela Makrela'),
                                                      ('2', '987654321', 'Mareczek');
insert into SERVICE_REQUEST (date,status,appuser_id,id,description)values
                                                                       ('2023-06-11','0','1','1','opis user 1 s 1'),
                                                                       ('2023-06-09','0','1','2','opis user 1 s 2'),
                                                                       ('2023-05-17','1','2','3','opis user 2 s 3'),
                                                                       ('2023-04-02','0','2','4','opis user 2 s 4');