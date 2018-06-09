package nktl.math.iterator;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 04.09.2017.
 */
public interface Iterator {

    int getPosition();
    Iterator setPosition(int pos);

    /**
     * Перемещает указатель в начало
     */
    void reset();
    void last();

    /**
     * Возвращает длину итерируемых данных. Если не может, вернет -1;
     */
    int length();

    /**
     * Возвращает true если есть слудеющий элемент
     */
    boolean hasNext();

    /**
     * Пробует перейти на следующий элемент и возвращает true в случае удачи.
     * В случае неудачи перемещает указатель на первый элемент
     */
    boolean next();

    /**
     * Методы, аналогичные {@link #next()} и {@link #hasNext()}, но идущие в обратную сторону
     */
    boolean hasPrevious();
    boolean previous();

    /**
     * Копирование. Возвращает итератор того же типа и на той же позиции, что и оригинал.
     */
    Iterator copy();

    /**
     * Копирование.
     * @param position - новая позиция. Если позиция не имеет смысла, возвращает null;
     */
    Iterator copy(int position);


    /**
     * РЕАЛИЗАЦИЯ АБСТРАКТНОГО ИТЕРАТОРА
     * Created by Zheka Grushevskiy, NAKATEEL, 04.09.2017.
     */
    // public
    abstract class AbsIterator implements Iterator {
        protected int position = 0;

        @Override
        public int getPosition() { return position; }
        @Override
        public Iterator setPosition(int pos) {
            position = pos;
            return this;
        }

        @Override
        public void reset() { position = 0; }

        @Override
        public boolean hasPrevious() { return position-1 >= 0; }

        @Override
        public Iterator copy(int position) {
            Iterator i = copy();
            i.setPosition(position);
            return i;
        }
    }
}
