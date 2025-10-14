import axios from 'axios';
import sinon from 'sinon';

import UnitBlockService from './unit-block.service';
import { UnitBlock } from '@/shared/model/unit-block.model';

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
  describe('UnitBlock Service', () => {
    let service: UnitBlockService;
    let elemDefault;

    beforeEach(() => {
      service = new UnitBlockService();
      elemDefault = new UnitBlock(
        123,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        0,
        0,
        0,
        'AAAAAAA',
        'AAAAAAA',
        0,
        0,
        'AAAAAAA',
        'AAAAAAA',
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { ...elemDefault };
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

      it('should create a UnitBlock', async () => {
        const returnedFromService = { id: 123, ...elemDefault };
        const expected = { ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a UnitBlock', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a UnitBlock', async () => {
        const returnedFromService = {
          normalizedType: 'BBBBBB',
          unitType: 'BBBBBB',
          bedroomsAmount: 'BBBBBB',
          unitBedrooms: 'BBBBBB',
          areaUnit: 'BBBBBB',
          unitsAmount: 1,
          unitsAreaFrom: 1,
          unitsAreaTo: 1,
          unitsAreaFromM2: 'BBBBBB',
          unitsAreaToM2: 'BBBBBB',
          unitsPriceFrom: 1,
          unitsPriceTo: 1,
          priceCurrency: 'BBBBBB',
          typicalImageUrl: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a UnitBlock', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a UnitBlock', async () => {
        const patchObject = {
          unitType: 'BBBBBB',
          areaUnit: 'BBBBBB',
          unitsAmount: 1,
          unitsAreaFrom: 1,
          unitsAreaTo: 1,
          unitsAreaFromM2: 'BBBBBB',
          unitsPriceTo: 1,
          priceCurrency: 'BBBBBB',
          typicalImageUrl: 'BBBBBB',
          ...new UnitBlock(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a UnitBlock', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of UnitBlock', async () => {
        const returnedFromService = {
          normalizedType: 'BBBBBB',
          unitType: 'BBBBBB',
          bedroomsAmount: 'BBBBBB',
          unitBedrooms: 'BBBBBB',
          areaUnit: 'BBBBBB',
          unitsAmount: 1,
          unitsAreaFrom: 1,
          unitsAreaTo: 1,
          unitsAreaFromM2: 'BBBBBB',
          unitsAreaToM2: 'BBBBBB',
          unitsPriceFrom: 1,
          unitsPriceTo: 1,
          priceCurrency: 'BBBBBB',
          typicalImageUrl: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of UnitBlock', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a UnitBlock', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a UnitBlock', async () => {
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
