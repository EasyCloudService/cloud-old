package net.easycloud.base.setup;

import java.util.Collection;

public record SetupBuilder<T>(String key, String question, Collection<T> possible){

    public boolean isAssignableFrom(Class<?> type) {
        return type.isAssignableFrom(getType());
    }

    private Class<T> getType() {
        return (Class<T>) Object.class;
    }

    public static <T> Builder<T> get() {
        return new Builder<>();
    }

    public static final class Builder<T> {
        private String key;
        private String question;
        private Object require;
        private Collection<T> possible;

        public Builder<T> key(String key) {
            this.key = key;
            return this;
        }

        public Builder<T> question(String question) {
            this.question = question;
            return this;
        }

        public Builder<T> requireObject(Object require) {
            this.possible = possible;
            return this;
        }

        public Builder<T> possibleResults(Collection<T> possible) {
            this.possible = possible;
            return this;
        }

        public SetupBuilder<T> build() {
            return new SetupBuilder<>(this.key, this.question, this.possible);
        }
    }
}
