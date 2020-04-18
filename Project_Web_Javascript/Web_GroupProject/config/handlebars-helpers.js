//set handlebars helpers

module.exports = {

    //set trueString if obj==value, else falseString
    ifEqual: (obj, value, trueString, falseString) => {
        return ( (obj.toString()==value.toString()) ? trueString : falseString );
    },

    //Display date
    toLocalDate: (date) => {
        return date.toLocaleString('default', { year: 'numeric',
            month: '2-digit', day: '2-digit' });
    },

    //Display date with hour
    toLocalDateHour: (date) => {
        return date.toLocaleString('default', {year: 'numeric',
            month: '2-digit', day: '2-digit' , hour: '2-digit', minute: '2-digit'})
    },

    //Transform date to fill input type date value
    toISODate: (date) => {
        return date.toISOString().split('T')[0];
    },

    //idem ifEqual but shearch in a list
    ifIn: (obj, list, trueString, falseString) => {
        for(let i=0; i<list.length; i++){
            if(list[i].toString()==obj.toString()){
                return trueString;
            }
        }
        return falseString;
    },

    ifAdminOrAuthor: (obj, user, author, list, trueString, falseString) => {
        if(user.toString()==author.toString()){
            return trueString;
        }
        for(let i=0; i<list.length; i++){
            if(list[i].toString()==obj.toString()){
                return trueString;
            }
        }
        return falseString;
    }
};