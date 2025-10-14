import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import UnitAvailabilityService from './unit-availability.service';
import { DATE_TIME_FORMAT } from '@/shared/composables/date-format';
import { UnitAvailability } from '@/shared/model/unit-availability.model';

const error = {
  response: {
    status: null,
    data: {
      type: null,
    },
  },
};

const axiosStub = {
  get: sinon.stub(axios, 'get'),
  post: sinon.stub(axios, 'post'),
  put: sinon.stub(axios, 'put'),
  patch: sinon.stub(axios, 'patch'),
  delete: sinon.stub(axios, 'delete'),
};

describe('Service Tests', () => {
  describe('UnitAvailability Service', () => {
    let service: UnitAvailabilityService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new UnitAvailabilityService();
      currentDate = new Date();
      elemDefault = new UnitAvailability(123, 'AAAAAAA', 0, 'AAAAAAA', 'AAAAAAA', currentDate, 'AAAAAAA', 0, 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { lastUpdated: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
        axiosStub.get.resolves({ data: returnedFromService });

        return service.find(123).then(res => {
          expect(res).toMatchObject(elemDefault);
        });
      });

      it('should not find an element', async () => {
        axiosStub.get.rejects(error);
        return service
          .find(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should create a UnitAvailability', async () => {
        const returnedFromService = { id: 123, lastUpdated: dayjs(currentDate).format(DATE_TIME_FORMAT), ...elemDefault };
        const expected = { lastUpdated: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a UnitAvailability', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a UnitAvailability', async () => {
        const returnedFromService = {
          buildingName: 'BBBBBB',
          areaFrom: 1,
          areaUnit: 'BBBBBB',
          bedroomType: 'BBBBBB',
          lastUpdated: dayjs(currentDate).format(DATE_TIME_FORMAT),
          priceCurrency: 'BBBBBB',
          priceFrom: 1,
          priceTo: 1,
          unitsAvailable: 1,
          ...elemDefault,
        };

        const expected = { lastUpdated: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a UnitAvailability', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a UnitAvailability', async () => {
        const patchObject = { areaFrom: 1, lastUpdated: dayjs(currentDate).format(DATE_TIME_FORMAT), ...new UnitAvailability() };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { lastUpdated: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a UnitAvailability', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of UnitAvailability', async () => {
        const returnedFromService = {
          buildingName: 'BBBBBB',
          areaFrom: 1,
          areaUnit: 'BBBBBB',
          bedroomType: 'BBBBBB',
          lastUpdated: dayjs(currentDate).format(DATE_TIME_FORMAT),
          priceCurrency: 'BBBBBB',
          priceFrom: 1,
          priceTo: 1,
          unitsAvailable: 1,
          ...elemDefault,
        };
        const expected = { lastUpdated: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of UnitAvailability', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a UnitAvailability', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a UnitAvailability', async () => {
        axiosStub.delete.rejects(error);

        return service
          .delete(123)
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });
    });
  });
});
