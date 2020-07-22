package com.akshar.one.util

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import com.akshar.one.view.noticeboard.adapter.MyNoticeBoardAdapter


class RecyclerItemTouchHelper(
    dragDirs: Int,
    swipeDirs: Int,
    private val listener: RecyclerItemTouchHelperListener
) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder != null) {
            var foregroundView : View? = null
            if((viewHolder as MyNoticeBoardAdapter.ViewHolder).binding!= null){
                foregroundView = (viewHolder as MyNoticeBoardAdapter.ViewHolder).binding?.rlForground
            }else {
                foregroundView = (viewHolder as MyNoticeBoardAdapter.ViewHolder).expiredBinding?.rlForground
            }
           // val foregroundView = (viewHolder as MyNoticeBoardAdapter.ViewHolder).binding?.rlForground

            getDefaultUIUtil().onSelected(foregroundView)
        }
    }

   override fun onChildDrawOver(
        c: Canvas, recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
       var foregroundView : View? = null
       if((viewHolder as MyNoticeBoardAdapter.ViewHolder).binding!= null){
           foregroundView = (viewHolder as MyNoticeBoardAdapter.ViewHolder).binding?.rlForground
       }else {
           foregroundView = (viewHolder as MyNoticeBoardAdapter.ViewHolder).expiredBinding?.rlForground
       }
       // val foregroundView = (viewHolder as MyNoticeBoardAdapter.ViewHolder).binding?.rlForground
        getDefaultUIUtil().onDrawOver(
            c, recyclerView, foregroundView, dX, dY,
            actionState, isCurrentlyActive
        )
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        var foregroundView : View? = null
        if((viewHolder as MyNoticeBoardAdapter.ViewHolder).binding!= null){
            foregroundView = (viewHolder as MyNoticeBoardAdapter.ViewHolder).binding?.rlForground
        }else {
            foregroundView = (viewHolder as MyNoticeBoardAdapter.ViewHolder).expiredBinding?.rlForground
        }
        //val foregroundView = (viewHolder as MyNoticeBoardAdapter.ViewHolder).binding?.rlForground
        getDefaultUIUtil().clearView(foregroundView)
    }

    override  fun onChildDraw(
        c: Canvas, recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
        actionState: Int, isCurrentlyActive: Boolean
    ) {
        var foregroundView : View? = null
        if((viewHolder as MyNoticeBoardAdapter.ViewHolder).binding!= null){
             foregroundView = (viewHolder as MyNoticeBoardAdapter.ViewHolder).binding?.rlForground
        }else {
            foregroundView = (viewHolder as MyNoticeBoardAdapter.ViewHolder).expiredBinding?.rlForground
        }


        getDefaultUIUtil().onDraw(
            c, recyclerView, foregroundView, dX, dY,
            actionState, isCurrentlyActive
        )
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwiped(viewHolder, direction, viewHolder.adapterPosition)
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    interface RecyclerItemTouchHelperListener {
        fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int, position: Int)
    }
}