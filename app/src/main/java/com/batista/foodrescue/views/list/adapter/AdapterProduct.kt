package com.batista.foodrescue.views.list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.batista.foodrescue.R
import com.batista.foodrescue.data.model.Produto
import com.batista.foodrescue.views.OrderViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class AdapterProduct(
    private val taskItemClickListener: ProdutoClickListener,
    private val orderViewModel: OrderViewModel


) : RecyclerView.Adapter<AdapterProduct.ProdutoViewHolder>() {

    private var listProduct = emptyList<Produto>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val layoutAdapter = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_cardview, parent, false)

        return ProdutoViewHolder(layoutAdapter)
    }

    class ProdutoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textNome = view.findViewById<TextView>(R.id.textNomeProduto)
        val textStatus = view.findViewById<TextView>(R.id.textStatus)
        val textData = view.findViewById<TextView>(R.id.textData)
        val textQtd = view.findViewById<TextView>(R.id.textQtd)
        val buttonExcluir = view.findViewById<ImageView>(R.id.buttonExcluir)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {

        val produto = listProduct[position]

        holder.textNome.text = produto.name
        holder.textStatus.text = produto.status
        holder.textData.text = produto.dueDate
        holder.textQtd.text = produto.assignetTo

        holder.buttonExcluir.setOnClickListener {
            showConfirmationDialog(produto, holder.itemView.context)
        }

        holder.itemView.setOnClickListener {
            taskItemClickListener.onTaskClicked(produto)
        }

    }

    override fun getItemCount(): Int {
        return listProduct.size
    }

    fun setData(produto: List<Produto>) {
        this.listProduct = produto
        notifyDataSetChanged()
    }

    fun getProdutoAt(position: Int): Produto {
        return listProduct[position]
    }

    fun swap(origem: Int, destino: Int) {
        Collections.swap(listProduct, origem, destino)
        notifyItemMoved(origem, destino)
    }

    private fun showConfirmationDialog(produto: Produto, context: Context) {
        MaterialAlertDialogBuilder(context)
            .setTitle("Atenção")
            .setMessage("Você está certo disso??")
            .setCancelable(false)
            .setNegativeButton("Não") {  _,_-> }
            .setPositiveButton("Sim, estou certo") { _,_->orderViewModel.deleteProduto(produto) }.show()
    }
}

