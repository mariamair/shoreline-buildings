import http from 'k6/http';
import { check } from 'k6';

export const options = {
    stages: [
        { duration: '30s', target: 5 },  // ramp up to 5 users
        { duration: '1m',  target: 5 },  // hold at 5 users
        { duration: '30s', target: 10 },  // ramp up to 10 users
        { duration: '1m',  target: 10 },  // hold at 10 users
        { duration: '30s', target: 0  },  // ramp down
    ],
    thresholds: {
        http_req_duration: ['p(95)<600'],  // 95% of requests under 600ms
    },
};

const query = `{
    buildingCountEntities {
    totalCount
    items {
      id
      buildingCount
      region {
        name
      }
    }
  }
}`;

export default function () {
    const res = http.post(
        `${__ENV.BASE_URL}/api/graphql`,
        JSON.stringify({ query }),
        { headers: { 'Content-Type': 'application/json' } }
    );

    check(res, {
        'status is 200': (r) => r.status === 200,
        'no errors': (r) => !JSON.parse(r.body).errors,
    });
}