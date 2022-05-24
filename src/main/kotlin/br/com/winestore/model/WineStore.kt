package br.com.winestore.model

import com.sun.istack.Nullable
import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class WineStore(
    @Nullable
    var codigoLoja: String? = null,

    @Nullable
    var faixaInicio: Long? = null,

    @Nullable
    var faixaFim: Long? = null,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as WineStore

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , codigoLoja = $codigoLoja , faixaInicio = $faixaInicio , faixaFim = $faixaFim )"
    }
}