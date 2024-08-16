package com.example.myrecyclerviewapp.model

sealed class ListItemUiModel {
    data class Title(val title: String) : ListItemUiModel()
    data class Cat(val cat: CatUiModel) : ListItemUiModel()
}