package com.generation.telasdesenvolvmed

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.generation.telasdesenvolvmed.databinding.FragmentCriarPostBinding
import com.generation.telasdesenvolvmed.databinding.FragmentEditPostBinding
import com.generation.telasdesenvolvmed.model.Medico
import com.generation.telasdesenvolvmed.model.Postagem
import com.generation.telasdesenvolvmed.model.Tema
import java.time.LocalDateTime


class EditPostFragment : Fragment() {

	private lateinit var binding: FragmentEditPostBinding
	private val mainViewModel: MainViewModel by activityViewModels()
	private var temaSelecionado = 0L
	private var postagemSelecionada: Postagem? = null

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		// Inflate the layout for this fragment
		binding = FragmentEditPostBinding.inflate(layoutInflater, container, false)

		carregarDados()

		mainViewModel.listTema()

		mainViewModel.myTemaResponse.observe(viewLifecycleOwner) { response ->
			Log.d("Requisicao", response.body().toString())
			spinnerTema(response.body()!!)
		}

		binding.botaoPublicar.setOnClickListener {
			inserirNoBanco()
		}

		binding.botaoVoltarFeed.setOnClickListener {
			findNavController().navigate(R.id.action_editPostFragment_to_postFragment)
		}

		return binding.root
	}

	private fun spinnerTema(listaTema: List<Tema>) {

		if (listaTema.isEmpty())
			throw IllegalArgumentException("Lista vazia!")

		binding.selecTemas.adapter =
			ArrayAdapter(
				requireContext(),
				androidx.transition.R.layout.support_simple_spinner_dropdown_item,
				listaTema
			)

		binding.selecTemas.onItemSelectedListener =
			object : AdapterView.OnItemSelectedListener {
				override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
					val selected = binding.selecTemas.selectedItem as Tema

					temaSelecionado = selected.id
				}

				override fun onNothingSelected(p0: AdapterView<*>?) {

				}
			}
	}

	private fun validarCampos(titulo: String, conteudo: String, anexo: String): Boolean {
		return (
				(titulo.isNotBlank() && titulo.length in 20..255) &&
						(conteudo.isNotBlank() && conteudo.length in 20..5000) &&
						(anexo.isNotBlank() && anexo.length in 10..500)
				)
	}

	private fun inserirNoBanco() {

		val titulo = binding.tituloText.text.toString()
		val conteudo = binding.postText.text.toString()
		val anexo = binding.textAnexo.text.toString()
		val medico = Medico(
			mainViewModel.medicoLogado.value?.body()?.id!!.toLong(), null,
			null, null
		)
		val dataPostagem = LocalDateTime.now().toString()
		val tema = Tema(temaSelecionado, null, null)

		if (validarCampos(titulo, conteudo, anexo)) {

			if (postagemSelecionada != null) {
				val postagem = Postagem(
					postagemSelecionada?.id!!, titulo, conteudo, anexo,
					dataPostagem, tema, medico
				)
				mainViewModel.addPostagem(postagem)

				Toast.makeText(context, "Postagem atualizada com sucesso!", Toast.LENGTH_LONG)
					.show()
				findNavController().navigate(R.id.action_editPostFragment_to_postFragment)

			} else {
				Toast.makeText(context, "Verifique os campos!", Toast.LENGTH_SHORT).show()
			}
		}
	}

	private fun carregarDados() {
		postagemSelecionada = mainViewModel.postagemSelecionada
		if (postagemSelecionada != null) {
			binding.tituloText.setText(postagemSelecionada?.titulo)
			binding.postText.setText(postagemSelecionada?.descricao)
			binding.textAnexo.setText(postagemSelecionada?.anexo)
		}
	}
}