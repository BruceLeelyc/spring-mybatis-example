package com.lixl.mybatis.demo.interceptor;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class PagedResult<T> extends BaseBean implements DomainBean, List<T> {

    /**
     * 总页数
     */
    private Integer totalPages = 0;

    /**
     * 总条数
     */
    private Integer totalCount = 0;

    /**
     * 分页内容
     */
    private List<T> data = new ArrayList<T>();

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public int size() {
        return getData().size();
    }

    @Override
    public boolean isEmpty() {
        return getData().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return getData().contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return getData().iterator();
    }

    @Override
    public Object[] toArray() {
        return getData().toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return getData().toArray(a);
    }

    @Override
    public boolean add(T t) {
        return getData().add(t);
    }

    @Override
    public boolean remove(Object o) {
        return getData().remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return getData().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return getData().addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return getData().addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return getData().removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return getData().retainAll(c);
    }

    @Override
    public void replaceAll(UnaryOperator<T> operator) {
        getData().replaceAll(operator);
    }

    @Override
    public void sort(Comparator<? super T> c) {
        getData().sort(c);
    }

    @Override
    public void clear() {
        getData().clear();
    }

    @Override
    public boolean equals(Object o) {
        return getData().equals(o);
    }

    @Override
    public int hashCode() {
        return getData().hashCode();
    }

    @Override
    public T get(int index) {
        return getData().get(index);
    }

    @Override
    public T set(int index, T element) {
        return getData().set(index, element);
    }

    @Override
    public void add(int index, T element) {
        getData().add(index, element);
    }

    @Override
    public T remove(int index) {
        return getData().remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return getData().indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return getData().lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return getData().listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return getData().listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return getData().subList(fromIndex, toIndex);
    }

    @Override
    public Spliterator<T> spliterator() {
        return getData().spliterator();
    }

    @Override
    public boolean removeIf(Predicate<? super T> filter) {
        return getData().removeIf(filter);
    }

    @Override
    public Stream<T> stream() {
        return getData().stream();
    }

    @Override
    public Stream<T> parallelStream() {
        return getData().parallelStream();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        getData().forEach(action);
    }
}
