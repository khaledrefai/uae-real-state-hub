import { vitest } from 'vitest';
import { type MountingOptions, shallowMount } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import MediaAssetUpdate from './media-asset-update.vue';
import MediaAssetService from './media-asset.service';
import AlertService from '@/shared/alert/alert.service';

import PropertyService from '@/entities/property/property.service';

type MediaAssetUpdateComponentType = InstanceType<typeof MediaAssetUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const mediaAssetSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<MediaAssetUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('MediaAsset Management Update Component', () => {
    let comp: MediaAssetUpdateComponentType;
    let mediaAssetServiceStub: SinonStubbedInstance<MediaAssetService>;

    beforeEach(() => {
      route = {};
      mediaAssetServiceStub = sinon.createStubInstance<MediaAssetService>(MediaAssetService);
      mediaAssetServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          mediaAssetService: () => mediaAssetServiceStub,
          propertyService: () =>
            sinon.createStubInstance<PropertyService>(PropertyService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(MediaAssetUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.mediaAsset = mediaAssetSample;
        mediaAssetServiceStub.update.resolves(mediaAssetSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(mediaAssetServiceStub.update.calledWith(mediaAssetSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        mediaAssetServiceStub.create.resolves(entity);
        const wrapper = shallowMount(MediaAssetUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.mediaAsset = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(mediaAssetServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        mediaAssetServiceStub.find.resolves(mediaAssetSample);
        mediaAssetServiceStub.retrieve.resolves([mediaAssetSample]);

        // WHEN
        route = {
          params: {
            mediaAssetId: `${mediaAssetSample.id}`,
          },
        };
        const wrapper = shallowMount(MediaAssetUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.mediaAsset).toMatchObject(mediaAssetSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        mediaAssetServiceStub.find.resolves(mediaAssetSample);
        const wrapper = shallowMount(MediaAssetUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
