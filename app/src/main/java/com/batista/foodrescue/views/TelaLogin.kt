package com.batista.foodrescue.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.batista.foodrescue.R
import com.batista.foodrescue.databinding.FragmentTelaLoginBinding


class TelaLogin : Fragment() {
    private var _binding: FragmentTelaLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTelaLoginBinding.inflate(inflater, container, false)
        binding.buttonLogin.setOnClickListener {
            val usuario = "foodrescue"
            val psw = "123"

            //Se for verdadeiro, vai pra tela de produtos
            if (usuario == binding.usuario.text.toString() && psw == binding.senha.text.toString()) {
                findNavController().navigate(R.id.action_fragmentTelaLogin_to_fragmentTelaListaProdutos)
            } else {
                Toast.makeText(requireContext(), "Email ou senha inválidos!", Toast.LENGTH_LONG)
                    .show()
            }
        }
        return binding.root
    }
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar!!.hide()
    }

}