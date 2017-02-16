
/**
 * @author  Eduardo Borges
 * @about   Estudante de Sistemas de Informação - UFS
 * @website http://github.com/eduardoborges
 * @version 1.0
 */

import java.io.FileNotFoundException;

public class TesteOrganizadorSequencial {

	public static void main(String[] args) {
		try{

			Aluno a = new Aluno(78, "Maelsu", "Rua 15", "aloha7@mail.com", (short) 22);
			Aluno b = new Aluno(16, "Eduardo", "Rua 10", "aloha5@mail.com", (short) 22);
			Aluno c = new Aluno(89, "Tonho", "Rua 31", "aloha75@mail.com", (short) 22);
			Aluno d = new Aluno(1, "Luandson", "Rua 11", "aloha5@mail.com", (short) 22);
			Aluno e = new Aluno(0, "Laisa", "Rua 49", "laisa@email.com", (short)14);
			Aluno f = new Aluno(15, "Raul", "Rua 11", "laisa@email.com", (short)14);



			// OrganizadorSimples organizadorSimples = new OrganizadorSimples("alunos.db");
			// organizadorSimples.addAluno(a);
			// organizadorSimples.addAluno(b);
			// organizadorSimples.addAluno(c);
			
			
			OrganizadorSequencial organizadorSequencial = new OrganizadorSequencial("alunos.db");
			
			organizadorSequencial.addAluno(a);			
			organizadorSequencial.addAluno(b);
			organizadorSequencial.addAluno(c);
			organizadorSequencial.addAluno(d);
			organizadorSequencial.addAluno(e);
			organizadorSequencial.addAluno(f);

			// // teste de get getPosition
			// int testePosition = organizador.getPosition(20);
			// System.out.println(testePosition);

			// // teste de getItemIndex
			// Aluno alunoIndex = organizador.getItemIndex(1);
			// System.out.println(alunoIndex.getNome());
			
			//System.out.println("Aluno procurado");
			//System.out.println(alunoProcurado.getNome());
			
			//System.out.println("Teste de posição: ");
			//System.out.println(organizador.getPosition(alunoProcurado.getMatric()));
			
			// Aluno alunoItem5 = organizador.getItemIndex(2);
			// System.out.println(alunoItem5.getNome());
			organizadorSequencial.showAllData();

			
			organizadorSequencial.delAluno(15);
			organizadorSequencial.delAluno(0);

			organizadorSequencial.showAllData();

			Aluno g = new Aluno(14, "Pablo", "Rua 11", "laisa@email.com", (short) 14);

			organizadorSequencial.addAluno(g);

			organizadorSequencial.showAllData();
			
		}
		catch(FileNotFoundException e){
			System.out.println(e);
		}
		
		

	}

}
