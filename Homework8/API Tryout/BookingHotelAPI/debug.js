const axios = require('axios');

async function getinfo(){
    const options = {
    method: 'GET',
    url: 'https://booking-com-api3.p.rapidapi.com/booking/blockAvailability',
    params: {
        room1: 'A',
        checkin: '2024-05-09',
        checkout: '2024-05-10',
        hotel_ids: '191981',
        guest_cc: 'us'
    },
    headers: {
        Accept: 'application/json',
        'X-RapidAPI-Key': '054999d8c2mshb298a4b5c4ddea0p15eb49jsnabf9c5983234',
        'X-RapidAPI-Host': 'booking-com-api3.p.rapidapi.com'
    }
    };

    try {
        const response = await axios.request(options);
        console.log(response.data.result.map(item => item.block).flat());
    } catch (error) {
        console.error(error);
    }    
}

getinfo();