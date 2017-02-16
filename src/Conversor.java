/**
 * @author  Eduardo Borges
 * @about   Estudante de Sistemas de Informação - UFS
 * @website http://github.com/eduardoborges
 * @version 1.0
 */


/**
 *   posicoes
 *   0      8        68         148      198     200
 *   ===============================================
 *   |matric|  nome  | endereco  |  email | curso  |
 *   ===============================================
 *
 */

import java.nio.ByteBuffer;


public class Conversor {
    public static ByteBuffer getBuffer(Aluno a){
        ByteBuffer buf = ByteBuffer.allocate(Aluno.TAM);
        buf.position(0);
        buf.putLong(a.getMatric());
        buf.position(8);
        buf.put((a.getNome()).getBytes());
        buf.position(68);
        buf.put((a.getEndereco()).getBytes());
        buf.position(148);
        buf.put((a.getEmail()).getBytes());
        buf.position(198);
        buf.putShort(a.getCurso());
        buf.position(0);

        return buf;
    }

    public static Aluno getAluno(ByteBuffer buf) {
        Aluno al = new Aluno();

        buf.position(0);

        // matricula
        al.setMatric(buf.getLong());

        // nome
        byte[] vNome = new byte[60];
        buf.get(vNome);
        String nome = new String(vNome);
        nome = nome.trim();   // retira os espacos vazios
        al.setNome(nome);

        // endereco
        byte[] vEnd = new byte[80];
        buf.get(vEnd);
        String end = new String(vEnd);
        end = end.trim();
        al.setEndereco(end);
        
        // email
        byte[] vEmail = new byte[50];
        buf.get(vEmail);
        String email = new String(vEmail);
        email = email.trim();
        al.setEmail(email);

        // curso
        al.setCurso(buf.getShort());
        
        buf.position(0);

        return al;
    }
    
}