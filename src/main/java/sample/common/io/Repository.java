package sample.common.io;

import java.util.List;
import java.util.Map;

import sample.common.entity.EntityBase;

/**
 * エンティティを永続化するためのレポジトリークラスが実装すべきインターフェースです。
 *
 * @param <E> エンティティに対する総称型パラメーター
 */
public interface Repository<K extends Comparable<K>, E extends EntityBase<K>> {

	/**
	 * IDでエンティティを検索する。（論理削除済みのエンティティは除外する。）
	 * エンティティが見つからない場合はEntityNotFoundExceptionが送出される。
	 * 
	 * @param id ID
	 * @return 検索結果のエンティティ
	 * @throws EntityNotFoundException エンティティが見つからない場合
	 */
	E findById(K id);

	/**
	 * エンティティを全件検索する。（論理削除済みのエンティティは除外する。）
	 * エンティティが見つからない場合は空のListが返る。
	 * 
	 * @return エンティティのリスト
	 * @throws EntityNotFoundException エンティティが見つからない場合
	 */
	List<E> findAll();

	/**
	 * エンティティを全件検索する。（論理削除済みのエンティティは除外する。）
	 * エンティティのIDをキーとするマップを返す。
	 * 
	 * @return エンティティのIDをキーとするマップ
	 * @throws EntityNotFoundException エンティティが見つからない場合
	 */
	Map<K, E> findAllAsMap();

	/**
	 * エンティティの属性値を照合して検索する
	 * @param example 検索対象の属性値を格納したオブジェクト
	 * @return 検索結果に一致したエンティティのリスト
	 */
	List<E> findByExample(E example);

	/**
	 * エンティティを新規作成する。
	 * @param data 作成対象のエンティティ
	 */
	void create(E data);

	/**
	 * エンティティを更新する。
	 * @param data 更新対象のエンティティ
	 */
	void update(E data);

	/**
	 * エンティティを論理削除する。
	 * @param data 論理削除対象のエンティティ
	 */
	void delete(K id);

}
