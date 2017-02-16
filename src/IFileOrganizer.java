/**
 * @author  Eduardo Borges
 * @about   Estudante de Sistemas de Informação - UFS
 * @website http://github.com/eduardoborges
 * @version 1.0
 */

public interface IFileOrganizer {
	public void addAluno (Aluno a);
	public Aluno getAluno (long matric);
	public Aluno delAluno(long matric);
}