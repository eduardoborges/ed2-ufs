import java.io.FileNotFoundException;

public class TesteOrganizadorSequencial {

	public static void main(String[] args) {
		try{
			OrganizadorSequencial organizador = new OrganizadorSequencial("alunos.db");
			
			Aluno a = new Aluno(20, "Maelsu", "Rua 15", "aloha7@mail.com", (short)22);
			Aluno b = new Aluno(64, "Eduardo", "Rua 10", "aloha5@mail.com", (short)22);
			Aluno c = new Aluno(85, "Tonho", "Rua 31", "aloha75@mail.com", (short)22);
			Aluno d = new Aluno(32, "Luandson", "Rua 11", "aloha5@mail.com", (short)22);
			
			organizador.addAluno(a);
			organizador.addAluno(b);
			organizador.addAluno(c);
			organizador.addAluno(d);

			Aluno alunoProcurado = organizador.getAluno(20);
			
			//System.out.println("Aluno procurado");
			//System.out.println(alunoProcurado.getNome());
			
			//System.out.println("Teste de posição: ");
			//System.out.println(organizador.getPosition(alunoProcurado.getMatric()));
			
			Aluno alunoItem5 = organizador.getItemIndex(2);
			System.out.println(alunoItem5.getNome());
			
		}
		catch(FileNotFoundException e){
			System.out.println(e);
		}
		
		

	}

}
