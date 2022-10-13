package com.generation.telasdesenvolvmed

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.generation.telasdesenvolvmed.api.Repository
import com.generation.telasdesenvolvmed.data.Login
import com.generation.telasdesenvolvmed.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,

    application: Application
) : AndroidViewModel(application) {

    private val _myTemaResponse = MutableLiveData<Response<List<Tema>>>()
    val myTemaResponse: LiveData<Response<List<Tema>>> = _myTemaResponse

    private val _myPostagemResponse = MutableLiveData<Response<List<Postagem>>>()
    val myPostagemResponse: LiveData<Response<List<Postagem>>> = _myPostagemResponse

    private val _myComentarioResponse = MutableLiveData<Response<List<Comentario>>>()
    val myComentarioResponse: LiveData<Response<List<Comentario>>> = _myComentarioResponse

    val pacienteLogado = MutableLiveData<Response<Paciente>>()

    var medicoLogado = MutableLiveData<Response<Medico>>()

    var cadastroVerificado = MutableLiveData<Response<Cadastro>>()
    var cadastroEmailAtivo = MutableLiveData<Response<Cadastro>>()

    val selectLogin: LiveData<List<Login>>

    var postagemSelecionada: Postagem? = null

    var comentarioSelecionado: Comentario? = null

    init {
        selectLogin = repository.selectLogin
    }

    fun listTema() {
        viewModelScope.launch {
            try {

                val response = repository.listTema()
                _myTemaResponse.value = response

            } catch (e: Exception) {
                Log.d("Erro", e.message.toString())
            }
        }
    }

    fun addPostagem(postagem: Postagem) {
        viewModelScope.launch {
            try {

                repository.addPostagem(postagem)

            } catch (e: Exception) {
                Log.d("Erro", e.message.toString())
            }
        }
    }

    fun listPostagem() {
        viewModelScope.launch {
            try {

                val response = repository.listPostagem()
                _myPostagemResponse.value = response

            } catch (e: Exception) {
                Log.d("Erro", e.message.toString())
            }
        }
    }

    fun listComentario(id: Long) {
        viewModelScope.launch {
            try {

                val response = repository.listComentario(id)
                _myComentarioResponse.value = response

            } catch (e: Exception) {
                Log.d("Erro", e.message.toString())
            }
        }
    }

    fun addComentario(comentario: Comentario, id: Long) {
        viewModelScope.launch {
            try {
                repository.addComentario(comentario)
                listComentario(id)
            } catch (e: Exception) {
                Log.d("Erro", e.message.toString())
            }
        }
    }

    fun addMedico(medico: MedicoCadastro, email: String) {
        viewModelScope.launch {
            try {
                repository.addMedico(medico)
                val response = repository.getCadastroMedicoByEmail(email)
                medicoLogado.value = response
            } catch (e: Exception) {
                Log.d("Erro", e.message.toString())
            }
        }
    }

    fun addPaciente(paciente: PacienteCadastro, email: String) {
        viewModelScope.launch {
            try {
                repository.addPaciente(paciente)
                val response = repository.getCadastroPacienteByEmail(email)
                pacienteLogado.value = response
            } catch (e: Exception) {
                Log.d("Erro", e.message.toString())
            }
        }
    }

    fun attMedico(medico: MedicoCadastro, email: String) {
        viewModelScope.launch {
            try {
                repository.attMedico(medico)
                val response = repository.getCadastroMedicoByEmail(email)
                medicoLogado.value = response
            } catch (e: Exception) {
                Log.d("Erro", e.message.toString())
            }
        }
    }

    fun attPaciente(paciente: PacienteCadastro, email: String) {
        viewModelScope.launch {
            try {
                repository.attPaciente(paciente)
                val response = repository.getCadastroPacienteByEmail(email)
                pacienteLogado.value = response
            } catch (e: Exception) {
                Log.d("Erro", e.message.toString())
            }
        }
    }

    fun getCadastroByEmail(email: String) {
        viewModelScope.launch {
            try {
                val responsePaciente = repository.getCadastroPacienteByEmail(email)
                pacienteLogado.value = responsePaciente
                val response = repository.getCadastroByEmail(email)
                cadastroVerificado.value = response
                val responseMedico = repository.getCadastroMedicoByEmail(email)
                medicoLogado.value = responseMedico
            } catch (e: Exception) {
                Log.d("Erro", e.message.toString())
            }
        }
    }

    fun getCadastro(email: String) {
        viewModelScope.launch {
            try {
                val response = repository.getCadastroByEmail(email)
                cadastroVerificado.value = response
            } catch (e: Exception) {
                Log.d("Erro", e.message.toString())
            }
        }
    }

    fun getCadastroAtualizaEmail(email: String) {
        viewModelScope.launch {
            try {
                val response = repository.getCadastroByEmail(email)
                cadastroEmailAtivo.value = response
            } catch (e: Exception) {
                Log.d("Erro", e.message.toString())
            }
        }
    }

    fun addLogin(login: Login) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLogin(login)
        }
    }

    fun nukeLogin() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.nukeTable()
        }
    }

    fun deletaPostagem(id: Long) {
        viewModelScope.launch {
            try {
                repository.deletaPostagem(id)
                listPostagem()
            } catch (e: Exception) {
                Log.d("Erro", e.message.toString())
            }
        }
    }

    fun updateComentario(comentario: Comentario) {
        viewModelScope.launch {
            try {
                repository.updateComentario(comentario)
            } catch (e: Exception) {
                Log.d("Erro", e.message.toString())
            }
        }
    }

    fun deletaComentario(id: Long, idPostagem: Long) {
        viewModelScope.launch {
            try {
                repository.deletaComentario(id)
                listComentario(idPostagem)
            } catch (e: Exception) {
                Log.d("Erro", e.message.toString())
            }
        }
    }

}