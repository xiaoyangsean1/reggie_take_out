package com.sean.reggie.dto;


import com.sean.reggie.entity.Setmeal;
import com.sean.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDTo extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
