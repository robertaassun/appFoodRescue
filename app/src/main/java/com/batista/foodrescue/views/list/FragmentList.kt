package com.batista.foodrescue.views.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.batista.foodrescue.R
import com.batista.foodrescue.data.model.Produto
import com.batista.foodrescue.databinding.FragmentProdutosBinding
import com.batista.foodrescue.views.OrderViewModel
import com.batista.foodrescue.views.SwipeToDelete
import com.batista.foodrescue.views.list.adapter.AdapterProduct
import com.batista.foodrescue.views.list.adapter.ProdutoClickListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FragmentList : Fragment(), ProdutoClickListener {

    private val viewModel: OrderViewModel by activityViewModels()

    private var _binding: FragmentProdutosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProdutosBinding.inflate(inflater, container, false)

        val adapter = AdapterProduct(this, viewModel)
        binding.fragmentProdutos.layoutManager = LinearLayoutManager(context)
        binding.fragmentProdutos.adapter = adapter
        binding.fragmentProdutos.setHasFixedSize(true)

        swipeDelete(binding.fragmentProdutos, adapter)

        viewModel.listProduct()
        lifecycleScope.launch {
            viewModel.myQueryResponse.collect { response ->
                adapter.setData(response)
            }
        }

        viewModel.progressBarVisibility.observe(viewLifecycleOwner){visibility ->
            binding.progressBar.visibility = visibility
        }

        binding.floatingActionButton.setOnClickListener {
            viewModel.productSelected = null
            findNavController().navigate(R.id.action_fragmentTelaListaProdutos_to_fragmentTelaForm)
        }
        return binding.root
    }

    override fun onTaskClicked(produto: Produto) {
        viewModel.productSelected = produto
        findNavController().navigate(R.id.action_fragmentTelaListaProdutos_to_fragmentTelaForm)
    }

    private fun swipeDelete(recyclerView: RecyclerView, adapter: AdapterProduct) {
        val swipeCallback = object : SwipeToDelete() {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val origem = viewHolder.adapterPosition
                val destino = target.adapterPosition
                adapter.swap(origem, destino)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val produtoAt = adapter.getProdutoAt(viewHolder.adapterPosition)
                viewModel.deleteProduto(produtoAt)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                restoreProductDelete(viewHolder.itemView, produtoAt)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    fun restoreProductDelete(view: View, produto: Produto) {
        val snackBar = Snackbar.make(view, "Deseja desfazer?", Snackbar.LENGTH_LONG)
        snackBar.setAction("Sim") {
            viewModel.addProduto(produto)
        }
        snackBar.show()
    }
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar!!.show()
    }

}