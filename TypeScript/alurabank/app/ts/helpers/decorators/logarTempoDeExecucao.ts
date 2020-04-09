export function logarTempoDeExecucao(segundos: boolean = false) {
    
    return function(target: any, propertyKey: string, descriptor: PropertyDescriptor) {
        const metodo = descriptor.value;

        descriptor.value = function(...args: any[]) {
            console.log("_______________________________");
            let t1 = performance.now();

            const retorno = metodo.apply(this, args);

            let t2 = performance.now();

            console.log(`La preformace de ${propertyKey} é ${segundos ? (t2 - t1) / 1000 : (t2 - t1)}`);
            
            return retorno;
        }

        return descriptor;
    }
}