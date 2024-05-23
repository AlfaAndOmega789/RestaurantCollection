INSERT INTO restaurants (id, name, city, estimated_cost, average_rating, votes) VALUES
	 (1, 'Dominos', 'Minsk', 30, 4.8574, 673),
	 (2, 'PapaJhons', 'Minsk', 50, 4.543, 300),
	 (3, 'PizzaTempo', 'Vitebsk', 24, 4.432, 543),
	 (4, 'Dominos', 'Vitebsk', 30, 4.6542, 3214),
	 (5, 'Dominos', 'Brest', 30, 4.34312, 34),
	 (6, 'KFC', 'Brest', 40, 4.51346, 654),
	 (7, 'Lisica', 'Minsk', 45, 4.6543, 765),
	 (8, 'BurgegKing', 'Minsk', 20, 4.64236, 432),
	 (9, 'KFC', 'Minsk', 40, 4.4321, 734);
------------------------------------------------------------------------------------------------------------------------
alter sequence res_id_seq restart 1000;