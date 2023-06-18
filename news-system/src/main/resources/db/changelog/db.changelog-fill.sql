--liquibase formatted sql

--changeset liquibase-demo-service:add-user-and-role
INSERT INTO news (title, text, time)
VALUES
    ('Breaking News: Scientists Discover New Planet',
    'Scientists have discovered a new planet outside our solar system...', '2022-05-27 09:30:00'),

    ('Local Woman Wins International Marathon',
     'A local woman has become the first person from her country to win an international marathon...',
      '2022-05-28 15:45:00'),

    ('New Study Shows Benefits of Meditation',
    'A new study has found that regular meditation can improve mental health and reduce stress...',
     '2022-05-29 11:00:00'),

    ('Sports Team Advances to National Championship',
     'A local sports team has advanced to the national championship after a thrilling victory over their rivals...',
      '2022-05-30 18:20:00'),

    ('Tech Company Launches Revolutionary New Product',
     'A tech company has launched a new product that promises to revolutionize the industry...',
      '2022-05-31 10:10:00'),

    ('World Leaders Gather for Climate Change Summit',
     'Leaders from around the world have gathered for a summit on climate change, where they will discuss ways to combat global warming...',
     '2022-06-01 14:00:00'),

    ('New Restaurant Opens in Downtown Area',
     'A new restaurant has opened in the downtown area, offering a unique dining experience...',
      '2022-06-02 19:30:00'),

    ('Actor Wins Award for Best Performance',
     'An actor has won an award for their performance in the latest blockbuster movie...',
      '2022-06-03 12:45:00'),

    ('Health Officials Investigate Outbreak of Illness',
    'Health officials are investigating an outbreak of an unknown illness that has affected dozens of people in the area...',
    '2022-06-04 08:15:00'),

    ('Local Charity Raises Millions for Cancer Research',
     'A local charity has raised millions of dollars for cancer research, thanks to the generosity of donors...',
      '2022-06-05 16:00:00'),

    ('New Art Exhibit Opens at Museum',
    'A new art exhibit has opened at the museum, showcasing the works of some of the world''s most renowned artists...',
     '2022-06-06 11:30:00'),

    ('Scientists Discover Cure for Rare Disease',
     'Scientists have discovered a cure for a rare disease that has affected thousands of people around the world...',
      '2022-06-07 17:00:00'),

    ('Local Business Owner Wins Entrepreneur of the Year Award',
     'A local business owner has been named as the Entrepreneur of the Year, thanks to their innovative business strategies...',
      '2022-06-08 13:20:00'),

    ('New Study Shows Benefits of Exercise',
     'A new study has found that regular exercise can improve physical health and reduce the risk of chronic diseases...',
      '2022-06-09 09:00:00'),

    ('Celebrity Couple Announces Engagement',
     'A celebrity couple has announced their engagement, sending fans into a frenzy...',
      '2022-06-10 14:50:00'),

    ('Local School Wins National Science Competition',
     'A local school has won a national science competition, beating out hundreds of other schools from around the country...',
      '2022-06-11 16:30:00'),

    ('New Film Receives Rave Reviews at Film Festival',
     'A new film has received rave reviews at a film festival, with critics praising the performances and direction...',
     '2022-06-12 11:15:00'),

    ('Major Company Announces Plans for Expansion',
    'A major company has announced plans for expansion, with the construction of a new headquarters and the hiring of hundreds of new employees...',
     '2022-06-13 18:00:00'),

    ('Local Musician Wins National Songwriting Competition',
     'A local musician has won a national songwriting competition, with their song being praised for its emotional depth and musicality...',
      '2022-06-14 12:10:00'),

    ('New Study Shows Benefits of Eating Vegetables',
     'Anew study has found that eating vegetables can improve overall health and reduce the risk of chronic diseases...',
      '2022-06-15 10:00:00');

INSERT INTO comments (time, text, username, news_id)
    SELECT
        now() - (interval '1 day' * random() * 30),
        'Comment ' || i || ' for news ' || news.id,
        'User ' || (i % 5 + 1),
        news.id
    FROM
        generate_series(1, 20) AS i,
        news
    WHERE
        i % 10 <> 0;

