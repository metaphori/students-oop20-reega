package reega.util;

import javafx.util.Pair;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ServiceCollection {

    private final Map<Class<?>, Object> singletonHashMap = new HashMap<>();
    private final Map<Class<?>, Function<ServiceProvider, ?>> transientHashMap = new HashMap<>();
    private ServiceProvider svcProvider;
    private boolean svcProviderAlreadyCreated;

    public ServiceCollection() {
        this.buildServiceProvider(false);
    }

    /**
     * Set the service provider.
     *
     * @param svcProvider service provider to set
     */
    private void setServiceProvider(final ServiceProvider svcProvider) {
        this.svcProvider = svcProvider;
    }

    /**
     * Adds a singleton with a constant value.
     *
     * @param <T>   Type of the class
     * @param type  Class type
     * @param value singleton value
     */
    public <T> void addSingleton(final Class<T> type, final T value) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(value);
        this.singletonHashMap.put(type, value);
    }

    /**
     * Adds a singleton with an implementation function.
     *
     * @param <T>                    Type of the class
     * @param type                   Class type
     * @param implementationFunction Function that uses {@link ServiceProvider} to build the instance of the singleton
     */
    public <T> void addSingleton(final Class<T> type,
            final Function<ServiceProvider, ? extends T> implementationFunction) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(implementationFunction);

        this.singletonHashMap.put(type, implementationFunction.apply(this.svcProvider));
    }

    /**
     * Adds a singleton based on the {@link Inject} annotated constructor or a no parameter constructor if no
     * {@link Inject} annotated constructor is found.
     *
     * @param <T>  Type of the class
     * @param type Class type
     * @throws IllegalArgumentException if {@code type} is an interface or an abstract class
     */
    public <T> void addSingleton(final Class<T> type) {
        Objects.requireNonNull(type);
        if (this.isInterfaceOrAbstractClass(type)) {
            throw new IllegalArgumentException("The type need to be a not abstract class");
        }
        this.singletonHashMap.put(type, this.createInstance(type));
    }

    /**
     * Adds a singleton based on the {@link Inject} annotated constructor of {@code implementationType} or a no
     * parameter constructor if no {@link Inject} annotated constructor is found.
     *
     * @param <T>                Interface type
     * @param interfaceType      Interface class
     * @param implementationType Implementation class
     * @throws IllegalArgumentException if {@code interfaceType} is not an interface or if {@code implementationType} is
     *                                  an interface or an abstract class
     */
    public <T> void addSingleton(final Class<T> interfaceType, final Class<? extends T> implementationType) {
        Objects.requireNonNull(interfaceType);
        Objects.requireNonNull(implementationType);
        if (!interfaceType.isInterface()) {
            throw new IllegalArgumentException("InterfaceType needs to be an interface");
        }
        if (this.isInterfaceOrAbstractClass(implementationType)) {
            throw new IllegalArgumentException("ImplementationType needs to be a not abstract class");
        }

        this.singletonHashMap.put(interfaceType, this.createInstance(implementationType));
    }

    /**
     * Adds a transient with an implementation function.
     *
     * @param <T>                    Type of the class
     * @param type                   Class type
     * @param implementationFunction Function that uses {@link ServiceProvider} to build the instance of the transient
     * @throws IllegalArgumentException if {@code type} is an interface or an abstract class
     */
    public <T> void addTransient(final Class<T> type, final Function<ServiceProvider, T> implementationFunction) {
        Objects.requireNonNull(type);
        Objects.requireNonNull(implementationFunction);
        this.transientHashMap.put(type, implementationFunction);
    }

    /**
     * Adds a transient based on the {@link Inject} annotated constructor or a no parameter constructor if no
     * {@link Inject} annotated constructor is found.
     *
     * @param <T>  Type of the class
     * @param type Class type
     * @throws IllegalArgumentException if {@code type} is an interface or an abstract class
     */
    public <T> void addTransient(final Class<T> type) {
        if (this.isInterfaceOrAbstractClass(type)) {
            throw new IllegalArgumentException("The type need to be a not abstract class");
        }
        this.addTransient(type, serviceProvider -> this.createInstance(type));
    }

    /**
     * Adds a transient based on the {@link Inject} annotated constructor of {@code implementationType} or a no
     * parameter constructor if no {@link Inject} annotated constructor is found.
     *
     * @param <T>                Interface type
     * @param interfaceType      Interface class
     * @param implementationType Implementation class
     * @throws IllegalArgumentException if {@code interfaceType} is not an interface or if {@code implementationType} is
     *                                  an interface or an abstract class
     */
    public <T> void addTransient(final Class<T> interfaceType, final Class<? extends T> implementationType) {
        Objects.requireNonNull(interfaceType);
        Objects.requireNonNull(implementationType);
        if (!interfaceType.isInterface()) {
            throw new IllegalArgumentException("InterfaceType needs to be an interface");
        }
        if (this.isInterfaceOrAbstractClass(implementationType)) {
            throw new IllegalArgumentException("ImplementationType needs to be a not abstract class");
        }
        this.addTransient(interfaceType, serviceProvider -> this.createInstance(implementationType));
    }

    /**
     * Get a service.
     *
     * @param <T>  Type of the service
     * @param type Class type
     * @return Instance of the service, null if it isn't found
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getService(final Class<T> type) {
        Objects.requireNonNull(type);

        if (this.singletonHashMap.containsKey(type)) {
            return Optional.of((T) this.singletonHashMap.get(type));
        } else if (this.transientHashMap.containsKey(type)) {
            return Optional.of((T) this.transientHashMap.get(type).apply(this.svcProvider));
        }
        return Optional.empty();
    }

    /**
     * Get the constructor and the parameters of the constructor of the class {@code type}.
     *
     * @param type Class type
     * @return a Pair of a constructor and its resolved parameters implementation
     */
    private Pair<Constructor<?>, Object[]> getConstructorAndParametersByType(final Class<?> type) {
        Objects.requireNonNull(type);

        final Constructor<?> constructor = Arrays.stream(type.getConstructors())
                .filter(ctor -> ctor.isAnnotationPresent(Inject.class))
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {

                    if (list.size() == 0) {
                        final Optional<Constructor<?>> tmpCtor = Arrays.stream(type.getConstructors())
                                .filter(ctor -> ctor.getParameterTypes().length == 0)
                                .findFirst();
                        if (tmpCtor.isEmpty()) {
                            throw new IllegalArgumentException(
                                    "You don't have a constructor annotated with the @Inject annotation, nor a constructor with no parameters");
                        }
                        return tmpCtor.get();
                    } else if (list.size() != 1) {
                        throw new IllegalArgumentException(
                                "You need to have only one constructor with @Inject annotation");
                    }
                    return list.get(0);
                }));

        final Object[] paramImplementation = Arrays.stream(constructor.getParameterTypes()).map(paramClass -> {
            @SuppressWarnings("unchecked")
            final Optional<Object> implementation = (Optional<Object>) this.getService(paramClass);
            if (implementation.isEmpty()) {
                throw new NoSuchElementException("You don't have a DI Service for it");
            }
            return implementation.get();
        }).toArray(Object[]::new);

        return new Pair<>(constructor, paramImplementation);
    }

    /**
     * Create an instance of the class {@code type}.
     *
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T> T createInstance(final Class<T> type) {
        final Pair<Constructor<?>, Object[]> constructorAndParameters = this.getConstructorAndParametersByType(type);

        try {
            return (T) constructorAndParameters.getKey().newInstance(constructorAndParameters.getValue());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            throw new IllegalArgumentException(
                    "Cannot create a new instance because of: " + ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * Build the service provider from this service collection.
     *
     * @return a service provider that manages this services
     * @throws IllegalStateException if a service provider has already been built with this collection
     */
    public ServiceProvider buildServiceProvider() {
        return this.buildServiceProvider(true);
    }

    /**
     * Build a service provider.
     *
     * @param checkForSecondCall true if you want to check if the service provider has been already built, false
     *                           otherwise
     * @return a service provider that manages this services
     * @throws IllegalStateException if {@code checkForSecondCall} is true and a service provider has already been built
     *                               with this service collection
     */
    private ServiceProvider buildServiceProvider(final boolean checkForSecondCall) {
        if (checkForSecondCall) {
            if (this.svcProviderAlreadyCreated) {
                throw new IllegalStateException("Cannot build the service provider twice");
            }
            this.svcProviderAlreadyCreated = true;
        }
        final ServiceProvider svcProvider = new ServiceProvider(this);
        this.setServiceProvider(svcProvider);
        return svcProvider;
    }

    /**
     * Check if a {@link Class} is an interface or an abstract class.
     *
     * @param clazz class to check
     * @return true if it an interface or an abstract class, false otherwise
     */
    private boolean isInterfaceOrAbstractClass(final Class<?> clazz) {
        return (clazz.getModifiers() & Modifier.ABSTRACT) != 0 || clazz.isInterface();
    }

}
