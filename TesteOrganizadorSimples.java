package ProjetoED2;

import java.io.FileNotFoundException;

public class TesteOrganizadorSimples {
	public static void main(String[] args) {
		try {
			IFileOrganizer org = new OrganizadorSimples("alunos.db");
			
			
			Aluno a = new Aluno(20, "Maelsu", "Rua 22", "aloha@mail.com", (short)22);
			org.addAluno(a);
			
			Aluno alunoProcurado = org.getAluno(20);
			
			System.out.print(alunoProcurado.getNome());
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
