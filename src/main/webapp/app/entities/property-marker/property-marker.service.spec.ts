import axios from 'axios';
import sinon from 'sinon';
import dayjs from 'dayjs';

import PropertyMarkerService from './property-marker.service';
import { DATE_FORMAT } from '@/shared/composables/date-format';
import { PropertyMarker } from '@/shared/model/property-marker.model';

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
  describe('PropertyMarker Service', () => {
    let service: PropertyMarkerService;
    let elemDefault;
    let currentDate: Date;

    beforeEach(() => {
      service = new PropertyMarkerService();
      currentDate = new Date();
      elemDefault = new PropertyMarker(
        123,
        0,
        'AAAAAAA',
        currentDate,
        0,
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        0,
        'AAAAAAA',
        'AAAAAAA',
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = { completionDate: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
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

      it('should create a PropertyMarker', async () => {
        const returnedFromService = { id: 123, completionDate: dayjs(currentDate).format(DATE_FORMAT), ...elemDefault };
        const expected = { completionDate: currentDate, ...returnedFromService };

        axiosStub.post.resolves({ data: returnedFromService });
        return service.create({}).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not create a PropertyMarker', async () => {
        axiosStub.post.rejects(error);

        return service
          .create({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should update a PropertyMarker', async () => {
        const returnedFromService = {
          extId: 1,
          area: 'BBBBBB',
          completionDate: dayjs(currentDate).format(DATE_FORMAT),
          latitude: 1,
          longitude: 1,
          name: 'BBBBBB',
          developer: 'BBBBBB',
          status: 'BBBBBB',
          saleStatus: 'BBBBBB',
          minPrice: 1,
          coverUrl: 'BBBBBB',
          coverJson: 'BBBBBB',
          ...elemDefault,
        };

        const expected = { completionDate: currentDate, ...returnedFromService };
        axiosStub.put.resolves({ data: returnedFromService });

        return service.update(expected).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not update a PropertyMarker', async () => {
        axiosStub.put.rejects(error);

        return service
          .update({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should partial update a PropertyMarker', async () => {
        const patchObject = {
          area: 'BBBBBB',
          completionDate: dayjs(currentDate).format(DATE_FORMAT),
          longitude: 1,
          name: 'BBBBBB',
          status: 'BBBBBB',
          saleStatus: 'BBBBBB',
          coverUrl: 'BBBBBB',
          ...new PropertyMarker(),
        };
        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = { completionDate: currentDate, ...returnedFromService };
        axiosStub.patch.resolves({ data: returnedFromService });

        return service.partialUpdate(patchObject).then(res => {
          expect(res).toMatchObject(expected);
        });
      });

      it('should not partial update a PropertyMarker', async () => {
        axiosStub.patch.rejects(error);

        return service
          .partialUpdate({})
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should return a list of PropertyMarker', async () => {
        const returnedFromService = {
          extId: 1,
          area: 'BBBBBB',
          completionDate: dayjs(currentDate).format(DATE_FORMAT),
          latitude: 1,
          longitude: 1,
          name: 'BBBBBB',
          developer: 'BBBBBB',
          status: 'BBBBBB',
          saleStatus: 'BBBBBB',
          minPrice: 1,
          coverUrl: 'BBBBBB',
          coverJson: 'BBBBBB',
          ...elemDefault,
        };
        const expected = { completionDate: currentDate, ...returnedFromService };
        axiosStub.get.resolves([returnedFromService]);
        return service.retrieve({ sort: {}, page: 0, size: 10 }).then(res => {
          expect(res).toContainEqual(expected);
        });
      });

      it('should not return a list of PropertyMarker', async () => {
        axiosStub.get.rejects(error);

        return service
          .retrieve()
          .then()
          .catch(err => {
            expect(err).toMatchObject(error);
          });
      });

      it('should delete a PropertyMarker', async () => {
        axiosStub.delete.resolves({ ok: true });
        return service.delete(123).then(res => {
          expect(res.ok).toBeTruthy();
        });
      });

      it('should not delete a PropertyMarker', async () => {
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
