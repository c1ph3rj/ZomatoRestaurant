package com.c1ph3r.zomatorestaurant.Enum;

public enum TypeOfCuisines {

    CHINESE(1),
    SOUTH_INDIAN(2),
    NORTH_INDIAN(3),
    DESSERTS(4),
    FAST_FOOD(5),
    AMERICAN(6),
    CONTINENTAL(7),
    ARABIAN(8);

    private final int FilterLevel;

    TypeOfCuisines(int FilterLevel){
        this.FilterLevel = FilterLevel;
    }

}
