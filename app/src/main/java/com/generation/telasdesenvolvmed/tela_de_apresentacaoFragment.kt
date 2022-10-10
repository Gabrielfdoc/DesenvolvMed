package com.generation.telasdesenvolvmed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.generation.telasdesenvolvmed.databinding.FragmentTelaDeApresentacaoBinding

class tela_de_apresentacaoFragment : Fragment() {

    private lateinit var binding: FragmentTelaDeApresentacaoBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTelaDeApresentacaoBinding.inflate(layoutInflater, container, false)

        binding.button2.setOnClickListener {
            mainViewModel.selectLogin.observe(viewLifecycleOwner){
                verificaLogin()
            }

        }

        return binding.root
    }

    private fun verificaLogin() {

        //mainViewModel.selectLogin.observe(viewLifecycleOwner){
            if(mainViewModel.selectLogin.value?.size!! >= 1){
                val email = mainViewModel.selectLogin.value?.get(0)?.email.toString()
                mainViewModel.getCadastroByEmail(email)
                //findNavController().navigate(R.id.action_tela_de_apresentacaoFragment_to_loginFragment)
                findNavController().navigate(R.id.action_tela_de_apresentacaoFragment_to_postFragment)
            } else{
                //findNavController().navigate(R.id.action_tela_de_apresentacaoFragment_to_loginFragment)
                findNavController().navigate(R.id.action_tela_de_apresentacaoFragment_to_loginFragment)
            }
        //}


    }


}