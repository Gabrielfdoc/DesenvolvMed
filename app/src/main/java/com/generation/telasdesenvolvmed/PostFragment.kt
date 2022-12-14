package com.generation.telasdesenvolvmed

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.generation.telasdesenvolvmed.adapter.PostagemAdapter
import com.generation.telasdesenvolvmed.adapter.PostagemClickListener
import com.generation.telasdesenvolvmed.databinding.FragmentPostBinding
import com.generation.telasdesenvolvmed.model.*


class PostFragment : Fragment(), PostagemClickListener {

    private lateinit var binding: FragmentPostBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        binding = FragmentPostBinding.inflate(layoutInflater, container, false)

        mainViewModel.pacienteLogado.observe(viewLifecycleOwner) { response ->
            if (response.body() != null) {
                binding.addPostButton.visibility = View.INVISIBLE
            }
        }

        mainViewModel.listPostagem()

        binding.swipeToRefresh.setOnRefreshListener {
            mainViewModel.listPostagem()
        }

        val postagemAdapter = PostagemAdapter(this, mainViewModel, requireContext())
        binding.recyclerPostagem.layoutManager = LinearLayoutManager(context)
        binding.recyclerPostagem.adapter = postagemAdapter
        binding.recyclerPostagem.setHasFixedSize(true)

        binding.addPostButton.setOnClickListener {
            mainViewModel.postagemSelecionada = null
            findNavController().navigate(R.id.action_postFragment_to_criarPostFragment)
        }

        mainViewModel.myPostagemResponse.observe(viewLifecycleOwner) { response ->
            if (response.body() != null) {
                postagemAdapter.setList(response.body()!!)
                binding.swipeToRefresh.isRefreshing = false
            }
        }

        binding.perfilButton.setOnClickListener {
            findNavController().navigate(R.id.action_postFragment_to_perfilFragment)
        }

        return binding.root
    }

    override fun onPostagemClickListener(postagem: Postagem) {
        mainViewModel.postagemSelecionada = postagem
        findNavController().navigate(R.id.action_postFragment_to_editPostFragment)
    }

    override fun onPostagemParaComentarioClickListener(postagem: Postagem) {
        mainViewModel.postagemSelecionada = postagem
        findNavController().navigate(R.id.action_postFragment_to_comentariosFragment)
    }
}