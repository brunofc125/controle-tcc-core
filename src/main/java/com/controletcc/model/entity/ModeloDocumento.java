package com.controletcc.model.entity;

import com.controletcc.model.entity.base.Arquivo;
import com.controletcc.model.enums.TipoTcc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModeloDocumento extends Arquivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descricao", nullable = false)
    private String descricao;

    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @Column(name = "tipo_tcc", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoTcc tipoTcc;

}
