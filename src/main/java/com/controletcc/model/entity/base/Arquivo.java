package com.controletcc.model.entity.base;

import com.controletcc.util.FileAppUtil;
import com.controletcc.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import java.io.IOException;

@Getter
@Setter
@MappedSuperclass
public class Arquivo extends BaseEntity {
    @Lob
    @Column(name = "conteudo", nullable = false)
    protected byte[] conteudo;

    @Column(name = "nome_arquivo", nullable = false)
    protected String nomeArquivo;

    @Column(name = "media_type", nullable = false)
    protected String mediaType;

    public String getBase64Conteudo() throws IOException {
        return FileAppUtil.byteToBase64(this.conteudo);
    }

    public void setBase64Conteudo(String base64Conteudo) throws IOException {
        this.conteudo = FileAppUtil.base64toByte(base64Conteudo);
    }

    public boolean isFileValid() {
        return conteudo != null && conteudo.length > 0 && !StringUtil.isNullOrBlank(nomeArquivo) && !StringUtil.isNullOrBlank(mediaType);
    }

}
