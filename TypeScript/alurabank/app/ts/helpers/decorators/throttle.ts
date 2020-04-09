export function throttle(ms = 500) {

    return function (target: any, propertyKey: string, descriptor: PropertyDescriptor) {
        const metodo = descriptor.value;
        
        let timer = 0;
        descriptor.value = function (...args: any[]) {
            event ? event.preventDefault() : null;

            clearInterval(timer);

            timer = setTimeout(() => {
                metodo.apply(this, args);
            }, ms)
        }

        return descriptor;
    }
}