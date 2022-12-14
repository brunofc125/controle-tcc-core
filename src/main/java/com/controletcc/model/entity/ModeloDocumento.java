package com.controletcc.model.entity;

import com.controletcc.model.enums.TipoTcc;
import com.controletcc.model.entity.base.Arquivo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "modelo_documento")
public class ModeloDocumento extends Arquivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @ElementCollection(targetClass = TipoTcc.class)
    @CollectionTable(name = "modelo_documento_tipo_tcc", joinColumns = @JoinColumn(name = "id_modelo_tcc"))
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_tcc", nullable = false)
    private Set<TipoTcc> tipoTccs;

    @Formula("(select string_agg(mdtt.tipo_tcc, ', ') from modelo_documento_tipo_tcc mdtt where mdtt.id_modelo_tcc = id)")
    private String tipoTccsNome;

}
