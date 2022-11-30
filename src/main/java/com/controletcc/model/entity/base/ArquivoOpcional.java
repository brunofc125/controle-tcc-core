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
public class ArquivoOpcional extends BaseEntity {

    @Lob
    @Column(name = "conteudo")
    protected byte[] conteudo;

    @Column(name = "nome_arquivo")
    protected String nomeArquivo;

    @Column(name = "media_type")
    protected String mediaType;

    public String getBase64Conteudo() throws IOException {
        return this.conteudo != null && this.conteudo.length > 0 ? FileAppUtil.byteToBase64(this.conteudo) : null;
    }

    public void setBase64Conteudo(String base64Conteudo) throws IOException {
        if (!StringUtil.isNullOrBlank(base64Conteudo)) {
            this.conteudo = FileAppUtil.base64toByte(base64Conteudo);
        }
    }

    public boolean isFileValid() {
        return conteudo != null && conteudo.length > 0 && !StringUtil.isNullOrBlank(nomeArquivo) && !StringUtil.isNullOrBlank(mediaType);
    }

}
