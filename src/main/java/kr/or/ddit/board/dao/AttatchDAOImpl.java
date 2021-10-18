package kr.or.ddit.board.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import kr.or.ddit.mybatis.CustomSqlSessionFactoryBuilder;
import kr.or.ddit.vo.AttatchVO;
import kr.or.ddit.vo.BoardVO;

public class AttatchDAOImpl implements AttatchDAO {

	private SqlSessionFactory sqlSessionFactory = CustomSqlSessionFactoryBuilder.getSqlSessionFactory();
	
	@Override
	public int insertAttatches(BoardVO board, SqlSession sqlSession) {
		return sqlSession.insert("kr.or.ddit.board.dao.AttatchDAO.insertAttatches", board);
	}

	@Override
	public AttatchVO selectAttatch(int attNo) {
		try(
				SqlSession sqlSession = sqlSessionFactory.openSession();
		){
			AttatchDAO mapper = sqlSession.getMapper(AttatchDAO.class);
			return mapper.selectAttatch(attNo);
		}
	}

	@Override
	public int deleteAttatches(BoardVO board, SqlSession sqlSession) {
		return sqlSession.delete("kr.or.ddit.board.dao.AttatchDAO.deleteAttatches", board);
	}

}












