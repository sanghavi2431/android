package `in`.woloo.www.application_kotlin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import `in`.woloo.www.application_kotlin.repositories.SayItWithWolooRepository
import `in`.woloo.www.application_kotlin.view_models.SayItWithWolooViewModel

class SayItWithWolooViewModelFactory(private val repository: SayItWithWolooRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SayItWithWolooViewModel::class.java)) {
            return SayItWithWolooViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
