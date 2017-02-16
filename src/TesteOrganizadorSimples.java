/**
 * @author  Eduardo Borges
 * @about   Estudante de Sistemas de Informação - UFS
 * @website http://github.com/eduardoborges
 * @version 1.0
 */

import java.io.FileNotFoundException;

public class TesteOrganizadorSimples {
	public static void main(String[] args) {
		try {
			IFileOrganizer org = new OrganizadorSimples("alunos.db");
			
			
			Aluno a = new Aluno(20, "Maelsu", "Rua 15", "aloha7@mail.com", (short)22);
			Aluno b = new Aluno(64, "Eduardo", "Rua 10", "aloha5@mail.com", (short)22);
			Aluno c = new Aluno(85, "Tonho", "Rua 31", "aloha75@mail.com", (short)22);
			Aluno d = new Aluno(32, "Luandson", "Rua 11", "aloha5@mail.com", (short)22);
			
			org.addAluno(a);
			org.addAluno(b);
			org.addAluno(c);
			org.addAluno(d);

			Aluno alunoProcurado = org.getAluno(20);
			Aluno alunoRemovido = org.delAluno(85);
			
			System.out.println("teste");
			System.out.println(alunoProcurado.getNome());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
