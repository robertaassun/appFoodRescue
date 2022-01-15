package com.batista.foodrescue.views

import androidx.recyclerview.widget.ItemTouchHelper

abstract class SwipeToDelete : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
) {

}