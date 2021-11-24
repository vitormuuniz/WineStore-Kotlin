package br.com.winestore.model

import com.sun.istack.Nullable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class WineStore (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Nullable
    var codigoLoja: String,

    @Nullable
    var faixaInicio: Long,

    @Nullable
    var faixaFim: Long,
)