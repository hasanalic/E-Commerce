package com.hasanalic.ecommerce.feature_location.presentation.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder<BINDING: ViewDataBinding>(val binder: BINDING) :
    RecyclerView.ViewHolder(binder.root) {
}